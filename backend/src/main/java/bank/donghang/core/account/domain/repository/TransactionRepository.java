package bank.donghang.core.account.domain.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

	private final TransactionJpaRepository transactionJpaRepository;

	public Transaction saveTransaction(Transaction transaction) {
		return transactionJpaRepository.save(transaction);
	}

	public boolean existsByAccountIdAndAmountAndTypeAndSessionStartTime(
		Long accountId,
		Long amount,
		LocalDateTime sessionStartTime,
		TransactionType type
	) {
		return transactionJpaRepository.existsByAccountIdAndAmountAndTypeAndSessionStartTime(
			accountId,
			amount,
			type,
			sessionStartTime
		);
	}
}
