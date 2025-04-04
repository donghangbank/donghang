package bank.donghang.core.ledger.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.ledger.domain.JournalLine;

public interface JournalLineJpaRepository extends JpaRepository<JournalLine, Long>, JournalLineJpaRepositoryCustom {
}
