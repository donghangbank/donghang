package bank.donghang.core.ledger.domain.repository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import bank.donghang.core.ledger.dto.query.TransactionSumDay;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LedgerRepository {

	private final LedgerJpaRepository ledgerJpaRepository;
	private final LedgerJpaRepositoryCustomImpl ledgerJpaRepositoryCustomImpl;

	public TransactionSumDay getTransactionDepositSumDay(
		LocalDateTime start,
		LocalDateTime end
	) {
		return ledgerJpaRepositoryCustomImpl.getTransactionDepositSumDay(
			start,
			end
		);
	}

	public TransactionSumDay getTransactionWithdrawalSumDay(
		LocalDateTime start,
		LocalDateTime end
	) {
		return ledgerJpaRepositoryCustomImpl.getTransactionWithdrawalSumDay(
			start,
			end
		);
	}
}
