package bank.donghang.core.ledger.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bank.donghang.core.ledger.domain.JournalEntry;
import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;

public interface JournalEntryJpaRepository extends JpaRepository<JournalEntry, Long> {

	@Modifying
	@Query("UPDATE JournalEntry je SET je.reconciliationStatus = :status WHERE je.id IN :ids")
	int updateJournalEntriesStatus(
			@Param("status") ReconciliationStatus status,
			@Param("ids") List<Long> ids
	);
}
