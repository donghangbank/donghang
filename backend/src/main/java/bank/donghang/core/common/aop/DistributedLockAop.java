package bank.donghang.core.common.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import bank.donghang.core.common.annotation.DistributedLock;
import bank.donghang.core.common.annotation.TransferDistributedLock;
import bank.donghang.core.common.parser.CustomSpringExpressionLanguageParser;
import bank.donghang.core.common.wrapper.AopForTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
	private static final String REDISSON_LOCK_PREFIX = "LOCK:";
	private static final int MAX_RETRIES = 5;
	private static final long RETRY_DELAY_MS = 500;

	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(bank.donghang.core.common.annotation.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		String key = REDISSON_LOCK_PREFIX + CustomSpringExpressionLanguageParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			distributedLock.key()
		);
		log.info("Generated lock key: {}", key);

		RLock rLock = redissonClient.getLock(key);

		int retryCount = 0;
		boolean lockAcquired = false;

		while (!lockAcquired && retryCount < MAX_RETRIES) {
			try {
				lockAcquired = rLock.tryLock(
					distributedLock.waitTime(),
					distributedLock.leaseTime(),
					distributedLock.timeUnit()
				);

				if (!lockAcquired) {
					retryCount++;
					log.warn("Failed to acquire lock for key: {}, retry count: {}/{}", key, retryCount, MAX_RETRIES);

					if (retryCount < MAX_RETRIES) {
						Thread.sleep(RETRY_DELAY_MS);
					}
				}
			} catch (InterruptedException e) {
				log.error("InterruptedException while acquiring lock for key: {}", key, e);
				Thread.currentThread().interrupt();
				throw new RuntimeException("Thread was interrupted while acquiring lock", e);
			}
		}

		if (!lockAcquired) {
			log.error("Failed to acquire lock after {} retries for key: {}", MAX_RETRIES, key);
			throw new RuntimeException("Failed to acquire distributed lock after maximum retries");
		}

		try {
			log.info("Lock acquired for key: {} after {} retries", key, retryCount);
			return aopForTransaction.proceed(joinPoint);
		} finally {
			try {
				rLock.unlock();
				log.info("Lock released for key: {}", key);
			} catch (IllegalMonitorStateException e) {
				log.info("Redisson Lock Already UnLock {} {}", method.getName(), key);
			}
		}
	}

	@Around("@annotation(bank.donghang.core.common.annotation.TransferDistributedLock)")
	public Object lockTwoAccounts(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		TransferDistributedLock distributedLock = method.getAnnotation(TransferDistributedLock.class);

		String key1 = REDISSON_LOCK_PREFIX + CustomSpringExpressionLanguageParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			distributedLock.key1()
		);
		String key2 = REDISSON_LOCK_PREFIX + CustomSpringExpressionLanguageParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			distributedLock.key2()
		);
		log.info("Generated lock key1: {}", key1);
		log.info("Generated lock key2: {}", key2);

		if (key1.compareTo(key2) > 0) {
			String temp = key1;
			key1 = key2;
			key2 = temp;
		}

		RLock rLock1 = redissonClient.getLock(key1);
		RLock rLock2 = redissonClient.getLock(key2);

		boolean lock1Acquired = false;
		boolean lock2Acquired = false;
		int retryCount = 0;

		while (retryCount < MAX_RETRIES) {
			try {
				lock1Acquired = rLock1.tryLock(
					distributedLock.waitTime(),
					distributedLock.leaseTime(),
					distributedLock.timeUnit()
				);

				if (!lock1Acquired) {
					retryCount++;
					log.warn("Failed to acquire first lock for key: {}, retry count: {}/{}", key1, retryCount,
						MAX_RETRIES);

					if (retryCount < MAX_RETRIES) {
						Thread.sleep(RETRY_DELAY_MS);
						continue;
					} else {
						log.error("Failed to acquire first lock after {} retries for key: {}", MAX_RETRIES, key1);
						throw new RuntimeException("Failed to acquire first distributed lock after maximum retries");
					}
				}

				lock2Acquired = rLock2.tryLock(
					distributedLock.waitTime(),
					distributedLock.leaseTime(),
					distributedLock.timeUnit()
				);

				if (!lock2Acquired) {
					try {
						rLock1.unlock();
						lock1Acquired = false;
					} catch (IllegalMonitorStateException e) {
						log.info("Redisson Lock Already UnLock {} {}", method.getName(), key1);
					}

					retryCount++;
					log.warn("Failed to acquire second lock for key: {}, releasing first lock and retrying: {}/{}",
						key2, retryCount, MAX_RETRIES);

					if (retryCount < MAX_RETRIES) {
						Thread.sleep(RETRY_DELAY_MS);
						continue;
					} else {
						log.error("Failed to acquire second lock after {} retries for key: {}", MAX_RETRIES, key2);
						throw new RuntimeException("Failed to acquire second distributed lock after maximum retries");
					}
				}

				log.info("Locks acquired for keys: {} and {} after {} retries", key1, key2, retryCount);
				return aopForTransaction.proceed(joinPoint);
			} catch (InterruptedException e) {
				log.error("InterruptedException while acquiring locks", e);
				Thread.currentThread().interrupt();
				throw new RuntimeException("Thread was interrupted while acquiring locks", e);
			} finally {
				if (lock2Acquired) {
					try {
						rLock2.unlock();
						log.info("Lock released for key: {}", key2);
					} catch (IllegalMonitorStateException e) {
						log.info("Redisson Lock Already UnLock {} {}", method.getName(), key2);
					}
				}
				if (lock1Acquired) {
					try {
						rLock1.unlock();
						log.info("Lock released for key: {}", key1);
					} catch (IllegalMonitorStateException e) {
						log.info("Redisson Lock Already UnLock {} {}", method.getName(), key1);
					}
				}
			}
		}

		throw new RuntimeException("Failed to acquire distributed locks after maximum retries");
	}
}