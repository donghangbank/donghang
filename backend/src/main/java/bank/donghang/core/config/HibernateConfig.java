package bank.donghang.core.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bank.donghang.core.common.inspector.QueryCountInspector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class HibernateConfig {

	private final QueryCountInspector queryCountInspector;

	@Bean
	public HibernatePropertiesCustomizer configureStatementInspector() {
		return hibernateProperties ->
			hibernateProperties.put(
				AvailableSettings.STATEMENT_INSPECTOR,
				queryCountInspector
			);
	}
}