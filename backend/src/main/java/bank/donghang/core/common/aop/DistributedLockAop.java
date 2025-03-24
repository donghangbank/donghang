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
import bank.donghang.core.common.parser.CustomSpringELParser;
import bank.donghang.core.common.wrapper.AopForTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
	private static final String REDISSON_LOCK_PREFIX = "LOCK:";

	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(bank.donghang.core.common.annotation.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			distributedLock.key()
		);
		log.info("Generated lock key: {}", key);

		RLock rLock = redissonClient.getLock(key);

		try {
			boolean available = rLock.tryLock(
				distributedLock.waitTime(),
				distributedLock.leaseTime(),
				distributedLock.timeUnit()
			);
			if (!available) {
				log.warn("Lock not available for key: {}", key);
				return false;
			}

			log.info("Lock acquired for key: {}", key);
			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			log.error("InterruptedException while acquiring lock for key: {}", key, e);
			throw new InterruptedException();
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

		String key1 = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			distributedLock.key1()
		);
		String key2 = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
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

		try {
			lock1Acquired = rLock1.tryLock(
				distributedLock.waitTime(),
				distributedLock.leaseTime(),
				distributedLock.timeUnit()
			);
			if (!lock1Acquired) {
				log.warn("Lock not available for key: {}", key1);
				return false;
			}

			lock2Acquired = rLock2.tryLock(
				distributedLock.waitTime(),
				distributedLock.leaseTime(),
				distributedLock.timeUnit()
			);
			if (!lock2Acquired) {
				log.warn("Lock not available for key: {}", key2);
				return false;
			}

			log.info("Lock acquired for key: {}", key1);
			log.info("Lock acquired for key: {}", key2);

			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			log.error("InterruptedException while acquiring lock for key: {}", key1, e);
			Thread.currentThread().interrupt();
			throw new RuntimeException("Thread was interrupted while acquiring lock", e);
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
}