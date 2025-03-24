package bank.donghang.core.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransferDistributedLock {

	/**
	 * key1() => 발신계좌번호
	 * key2() => 수신계좌번호
	 * timeUnit() => 락의 시간 단위
	 * waitTime() => 락을 기다리는 시간 (기본 5초)
	 * leaseTime() => 락 임대 시간 (기본 3초)
	 */
	String key1();

	String key2();

	TimeUnit timeUnit() default TimeUnit.SECONDS;

	long waitTime() default 5L;

	long leaseTime() default 3L;
}
