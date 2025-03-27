package bank.donghang.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import bank.donghang.core.common.interceptor.QueryLoggingInterceptor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

	private final QueryLoggingInterceptor queryLoggingInterceptor;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("*")
			.allowedMethods("*")
			.allowedHeaders("*")
			.allowCredentials(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(queryLoggingInterceptor)
			.excludePathPatterns(
				"/css/**",
				"/images/**",
				"/js/**"
			);
	}
}

