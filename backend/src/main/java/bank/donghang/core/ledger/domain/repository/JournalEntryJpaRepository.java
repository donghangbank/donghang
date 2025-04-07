package bank.donghang.core.ledger.domain.repository;

import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.ledger.domain.JournalEntry;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JournalEntryJpaRepository extends JpaRepository<JournalEntry, Long> {

	@Modifying
	@Query("UPDATE JournalEntry je SET je.reconciliationStatus = :status WHERE je.id IN :ids")
	int updateJournalEntriesStatus(
			@Param("status") ReconciliationStatus status,
			@Param("ids") List<Long> ids
	);
}
