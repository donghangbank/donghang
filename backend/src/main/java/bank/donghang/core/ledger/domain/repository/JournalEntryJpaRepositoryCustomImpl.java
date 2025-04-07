package bank.donghang.core.ledger.domain.repository;

import static bank.donghang.core.account.domain.QTransaction.*;
import static bank.donghang.core.ledger.domain.QJournalEntry.*;
import static bank.donghang.core.ledger.domain.QJournalLine.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JournalEntryJpaRepositoryCustomImpl implements JournalEntryJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<DailyReconciliationQuery> getDailyReconciliationInfo(
		LocalDateTime start,
		LocalDateTime end
	) {
		return queryFactory.select(
				Projections.constructor(
					DailyReconciliationQuery.class,
					journalEntry.transactionId,
					transaction.type,
					transaction.status,
					journalEntry.id,
					journalLine.id,
					journalLine.amount,
					journalLine.accountId,
					journalLine.entryType,
					transaction.sessionStartTime
				))
			.from(journalEntry)
			.join(transaction)
			.on(journalEntry.transactionId.eq(transaction.id))
			.join(journalLine)
			.on(journalEntry.id.eq(journalLine.journalEntryId))
			.where(transaction.sessionStartTime.between(start, end)
				.and(journalEntry.reconciliationStatus.eq(ReconciliationStatus.PENDING)))
			.fetch();
	}
}