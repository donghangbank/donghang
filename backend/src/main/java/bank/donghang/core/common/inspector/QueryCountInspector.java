package bank.donghang.core.common.inspector;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class QueryCountInspector implements StatementInspector {

	private final ThreadLocal<Counter> queryCounter = new ThreadLocal<>();

	@Override
	public String inspect(String sql) {
		Counter counter = queryCounter.get();
		if (counter != null) {
			counter.increaseCount();
		}

		return sql;
	}

	public void startCounter() {
		queryCounter.set(new Counter(0L, System.currentTimeMillis()));
	}

	public void clearCounter() {
		queryCounter.remove();
	}

	public Long getCount() {
		return queryCounter.get().getCount();
	}

	public Long getTimeStamp() {
		return queryCounter.get().getTime();
	}

	@Getter
	class Counter {
		private Long count;
		private Long time;

		public Counter(Long count, Long time) {
			this.count = count;
			this.time = time;
		}

		public void increaseCount() {
			count++;
		}
	}
}
