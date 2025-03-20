package bank.donghang.core.common.aop;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.common.annotation.DistributedLock;
import bank.donghang.core.common.annotation.SingleAccountLock;
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

	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(bank.donghang.core.common.annotation.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		Object[] args = joinPoint.getArgs();
		TransactionRequest request = (TransactionRequest)args[0];
		long accountId1 = request.sendingAccountId();
		long accountId2 = request.receivingAccountId();

		String key1 = REDISSON_LOCK_PREFIX + Math.min(accountId1, accountId2);
		String key2 = REDISSON_LOCK_PREFIX + Math.max(accountId1, accountId2);

		log.info("Lock keys: key1={}, key2={}", key1, key2);

		RLock lock1 = redissonClient.getLock(key1);
		RLock lock2 = redissonClient.getLock(key2);

		if (lock1 == null || lock2 == null) {
			throw new IllegalStateException("Failed to create locks. lock1=" + lock1 + ", lock2=" + lock2);
		}

		RLock multiLock = redissonClient.getMultiLock(lock1, lock2);

		try {
			log.info("Trying to acquire locks for keys: {} and {}", key1, key2);
			boolean isLockAcquired = multiLock.tryLock(
				distributedLock.waitTime(),
				distributedLock.leaseTime(),
				TimeUnit.SECONDS
			);
			if (!isLockAcquired) {
				throw new IllegalStateException("Unable to acquire locks for keys: " + key1 + ", " + key2);
			}
			log.info("Successfully acquired locks for keys: {} and {}", key1, key2);

			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Interrupted while trying to acquire locks for keys: {} and {}", key1, key2, e);
			throw new InterruptedException("Lock acquisition was interrupted");
		} finally {
			try {
				if (multiLock != null) {
					multiLock.unlock();
					log.info("Released locks for keys: {} and {}", key1, key2);
				}
			} catch (IllegalMonitorStateException e) {
				log.warn("Redisson Locks Already Unlocked {} {}", method.getName(), key1 + ", " + key2);
			}
		}
	}

	@Around("@annotation(bank.donghang.core.common.annotation.SingleAccountLock)")
	public Object singleLock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		SingleAccountLock singleAccountLock = method.getAnnotation(SingleAccountLock.class);

		String key = REDISSON_LOCK_PREFIX + CustomSpringExpressionLanguageParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			singleAccountLock.key()
		);

		log.info("Lock key: {}", key);

		RLock rLock = redissonClient.getLock(key);

		if (rLock == null) {
			throw new IllegalStateException("Failed to create lock for key: " + key);
		}

		try {
			log.info("Trying to acquire lock for key: {}", key);
			boolean isLockAcquired = rLock.tryLock(
				singleAccountLock.waitTime(),
				singleAccountLock.leaseTime(),
				TimeUnit.SECONDS
			);
			if (!isLockAcquired) {
				throw new IllegalStateException("Unable to acquire lock for key: " + key);
			}
			log.info("Successfully acquired lock for key: {}", key);

			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Interrupted while trying to acquire lock for key: {}", key, e);
			throw new InterruptedException("Lock acquisition was interrupted");
		} finally {
			try {
				if (rLock != null && rLock.isHeldByCurrentThread()) {
					rLock.unlock();
					log.info("Released lock for key: {}", key);
				}
			} catch (IllegalMonitorStateException e) {
				log.warn("Redisson Lock Already Unlocked {} {}", method.getName(), key);
			}
		}
	}
}
