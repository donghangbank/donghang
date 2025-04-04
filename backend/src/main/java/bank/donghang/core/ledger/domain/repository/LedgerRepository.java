package bank.donghang.core.ledger.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import bank.donghang.core.ledger.domain.JournalEntry;
import bank.donghang.core.ledger.domain.JournalLine;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LedgerRepository {

	private final JournalEntryJpaRepository journalEntryJpaRepository;
	private final JournalLineJpaRepository journalLineJpaRepository;

	public void saveJournalEntry(JournalEntry journalEntry) {
		journalEntryJpaRepository.save(journalEntry);
	}

	public void saveJournalLine(JournalLine journalLine) {
		journalLineJpaRepository.save(journalLine);
	}

	public List<DailyReconciliationQuery> getDailyReconciliationQuery(
		LocalDateTime start,
		LocalDateTime end
	) {
		return null;
	}
}
