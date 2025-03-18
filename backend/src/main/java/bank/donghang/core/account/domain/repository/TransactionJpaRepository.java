package bank.donghang.core.account.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.account.domain.Transaction;

public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
}
