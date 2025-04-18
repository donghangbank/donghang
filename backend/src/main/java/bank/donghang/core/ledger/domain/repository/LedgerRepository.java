package bank.donghang.core.ledger.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import bank.donghang.core.ledger.domain.JournalEntry;
import bank.donghang.core.ledger.domain.JournalLine;
import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LedgerRepository {

	private final JournalEntryJpaRepository journalEntryJpaRepository;
	private final JournalEntryJpaRepositoryCustomImpl journalEntryJpaRepositoryCustom;
	private final JournalLineJpaRepository journalLineJpaRepository;
	private final JournalLineJdbcRepository journalLineJdbcRepository;

	public void saveJournalEntry(JournalEntry journalEntry) {
		journalEntryJpaRepository.save(journalEntry);
	}

	public void saveJournalLine(JournalLine journalLine) {
		journalLineJpaRepository.save(journalLine);
	}

	public List<DailyReconciliationQuery> getDailyReconciliationInfo(
		LocalDateTime start,
		LocalDateTime end
	) {
		return journalEntryJpaRepositoryCustom.getDailyReconciliationInfo(
			start,
			end
		);
	}

	public List<JournalEntry> getAllJournalEntriesIn(List<Long> journalEntryIds) {
		return journalEntryJpaRepository.findAllById(journalEntryIds);
	}

	public int updateJournalEntriesStatus(ReconciliationStatus status, List<Long> journalEntryIds) {
		return journalEntryJpaRepository.updateJournalEntriesStatus(
				status,
				journalEntryIds
		);
	}

	public void bulkInsertJournalLines(List<JournalLine> journalLines) {
		journalLineJdbcRepository.batchInsert(journalLines);
	}
}
