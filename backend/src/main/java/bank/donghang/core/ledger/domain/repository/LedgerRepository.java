package bank.donghang.core.ledger.domain.repository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LedgerRepository {

	private final JournalEntryJpaRepository journalEntryJpaRepository;
	private final JournalLineJpaRepository journalLineJpaRepository;

}
