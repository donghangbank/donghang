package bank.donghang.core.account.domain.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionType;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long>, TransactionJpaRepositoryCustom {
	boolean existsByAccountIdAndAmountAndTypeAndCreatedAt(Long accountId, Long amount, TransactionType type,
		LocalDateTime createdAt);

	boolean existsByAccountIdAndAmountAndTypeAndSessionStartTime(Long accountId, Long amount, TransactionType type,
		LocalDateTime sessionStartTime);
}
