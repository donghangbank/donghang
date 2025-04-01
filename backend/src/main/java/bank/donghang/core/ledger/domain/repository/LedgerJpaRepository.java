package bank.donghang.core.ledger.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.ledger.domain.Ledger;

public interface LedgerJpaRepository extends JpaRepository<Ledger, Long>, LedgerJpaRepositoryCustom {
}
