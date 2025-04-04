package bank.donghang.core.ledger.domain.repository;

import org.springframework.stereotype.Repository;

import bank.donghang.core.ledger.domain.JournalEntry;
import bank.donghang.core.ledger.domain.JournalLine;
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

}
