package bank.donghang.core.ledger.domain.repository;

import static bank.donghang.core.ledger.domain.QLedger.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import bank.donghang.core.ledger.domain.enums.LedgerStatus;
import bank.donghang.core.ledger.dto.query.TransactionSumDay;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LedgerJpaRepositoryCustomImpl implements LedgerJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public TransactionSumDay getTransactionDepositSumDay(
		LocalDateTime start,
		LocalDateTime end
	) {
		return queryFactory.select(
				Projections.constructor(
					TransactionSumDay.class,
					ledger.depositAmount.sum(),
					ledger.createdAt
				))
			.from(ledger)
			.where(
				ledger.createdAt.between(start, end)
					.and(ledger.ledgerStatus.eq(LedgerStatus.COMPLETED))
			)
			.groupBy(ledger.createdAt)
			.fetchOne();
	}

	@Override
	public TransactionSumDay getTransactionWithdrawalSumDay(
		LocalDateTime start,
		LocalDateTime end
	) {
		return queryFactory.select(
				Projections.constructor(
					TransactionSumDay.class,
					ledger.withdrawalAmount.sum(),
					ledger.createdAt
				))
			.from(ledger)
			.where(
				ledger.createdAt.between(start, end)
					.and(ledger.ledgerStatus.eq(LedgerStatus.COMPLETED))
			)
			.groupBy(ledger.createdAt)
			.fetchOne();
	}
}