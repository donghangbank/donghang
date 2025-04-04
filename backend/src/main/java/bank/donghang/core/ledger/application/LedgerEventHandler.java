// package bank.donghang.core.ledger.application;
//
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.event.TransactionalEventListener;
//
// import bank.donghang.core.ledger.domain.JournalEntry;
// import bank.donghang.core.ledger.domain.repository.LedgerRepository;
// import bank.donghang.core.ledger.dto.event.LedgerCreateEvent;
// import lombok.RequiredArgsConstructor;
//
// @Component
// @RequiredArgsConstructor
// public class LedgerEventHandler {
//
// 	private final LedgerRepository ledgerRepository;
//
// 	@TransactionalEventListener
// 	public void handleLedgerCreated(LedgerCreateEvent event) {
// 		JournalEntry senderJournalEntry = JournalEntry.create(
//
// 		);
// 	}
// }
