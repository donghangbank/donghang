package bank.donghang.donghang_api.bank.domain.repository;

import bank.donghang.donghang_api.bank.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankJpaRepository extends JpaRepository<Bank, Long> {
}
