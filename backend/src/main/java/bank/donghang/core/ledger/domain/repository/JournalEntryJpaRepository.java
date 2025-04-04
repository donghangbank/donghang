package bank.donghang.core.ledger.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.ledger.domain.JournalEntry;

public interface JournalEntryJpaRepository extends JpaRepository<JournalEntry, Long> {
}
