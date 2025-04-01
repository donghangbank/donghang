package bank.donghang.core.ledger.domain.repository;

import java.time.LocalDateTime;

import bank.donghang.core.ledger.dto.query.TransactionSumDay;

public interface LedgerJpaRepositoryCustom {

	TransactionSumDay getTransactionDepositSumDay(
		LocalDateTime start,
		LocalDateTime end
	);

	TransactionSumDay getTransactionWithdrawalSumDay(
		LocalDateTime start,
		LocalDateTime end
	);
}
