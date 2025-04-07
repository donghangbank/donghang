package bank.donghang.core.ledger.domain.repository;

import static bank.donghang.core.ledger.domain.QJournalEntry.*;
import static bank.donghang.core.ledger.domain.QJournalLine.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JournalLineJpaRepositoryImpl implements JournalLineJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<DailyReconciliationQuery> getDailyJournalLines(
		LocalDateTime start,
		LocalDateTime end
	) {
		return queryFactory.select(
				Projections.constructor(
					DailyReconciliationQuery.class,
					journalEntry.transactionId,
					journalLine.journalEntryId,
					journalLine.id,
					journalLine.amount,
					journalLine.accountId,
					journalLine.entryType
				))
			.from(journalLine)
			.leftJoin(journalEntry)
			.on(journalLine.journalEntryId.eq(journalEntry.id))
			.where(journalEntry.reconciliationStatus.eq(ReconciliationStatus.PENDING)
				.and(journalLine.createdAt.between(start, end)))
			.fetch();
	}
}
