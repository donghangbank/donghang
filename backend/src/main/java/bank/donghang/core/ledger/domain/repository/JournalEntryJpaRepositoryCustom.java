package bank.donghang.core.ledger.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;

public interface JournalEntryJpaRepositoryCustom {

	List<DailyReconciliationQuery> getDailyReconciliationInfo(
		LocalDateTime start,
		LocalDateTime end
	);
}
