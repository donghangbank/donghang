package bank.donghang.core.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@EnableAsync
@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {

	@Override
	@Bean(name = "ledgerExecutor")
	public Executor getAsyncExecutor() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(availableProcessors);
		executor.setMaxPoolSize(availableProcessors * 2);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("LedgerAsync-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		log.info("Initialized LedgerExecutor - Core: {}, Max: {}, Queue: {}", availableProcessors,
			availableProcessors * 2, 100);
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> {
			log.error("Async method '{}' threw an exception with params: {}", method.getName(), params, ex);
		};
	}
}