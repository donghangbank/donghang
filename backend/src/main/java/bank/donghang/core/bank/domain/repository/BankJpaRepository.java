package bank.donghang.core.bank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.bank.domain.Bank;

public interface BankJpaRepository extends JpaRepository<Bank, Long> {
}
