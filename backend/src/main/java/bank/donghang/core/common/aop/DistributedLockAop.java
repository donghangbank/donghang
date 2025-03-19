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

		String key = REDISSON_LOCK_PREFIX + CustomSpringExpressionLanguageParser.getDynamicValue(
			signature.getParameterNames(),
			joinPoint.getArgs(),
			distributedLock.key()
		);
		RLock rLock = redissonClient.getLock(key);

		try {
			log.info("Trying to acquire lock with key: {}", key);
			rLock.lock(distributedLock.leaseTime(), distributedLock.timeUnit());
			log.info("Successfully acquired lock with key: {}", key);

			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.info("Interrupted while trying to acquire lock with key: {}", key);
			throw new InterruptedException("Lock acquisition was interrupted");
		} finally {
			try {
				rLock.unlock();
				log.info("Released lock with key: {}", key);
			} catch (IllegalMonitorStateException e) {
				log.info("Redisson Lock Already Unlocked {} {}", method.getName(), key);
			}
		}
	}
}
