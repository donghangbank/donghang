package bank.donghang.core.account.domain.repository;

import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.Transaction;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

	private final TransactionJpaRepository transactionJpaRepository;

	public Transaction saveTransaction(Transaction transaction) {
		return transactionJpaRepository.save(transaction);
	}
}
