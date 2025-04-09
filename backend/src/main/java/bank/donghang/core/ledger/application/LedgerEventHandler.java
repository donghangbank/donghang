package bank.donghang.core.ledger.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.ledger.domain.JournalEntry;
import bank.donghang.core.ledger.domain.JournalEntryInfo;
import bank.donghang.core.ledger.domain.JournalLine;
import bank.donghang.core.ledger.domain.enums.EntryType;
import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
import bank.donghang.core.ledger.domain.repository.LedgerRepository;
import bank.donghang.core.ledger.dto.event.DepositEvent;
import bank.donghang.core.ledger.dto.event.TransferEvent;
import bank.donghang.core.ledger.dto.event.WithdrawalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LedgerEventHandler {

	//TODO: 벌크 인서트 고민

	@Value("${DONGHANG_CASH_ACCOUNT_ID}")
	private Long bankCashAccountId;

	//	@Value("${DONGHANG_ASSET_ACCOUNT_ID}")
	//	private Long bankAssetAccountId;

	private final LedgerRepository ledgerRepository;

	@Async("ledgerExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleTransfer(TransferEvent transferEvent) {

		List<JournalLine> journalLines = new ArrayList<>();

		JournalEntry transferEntry = createJournalEntry(
			transferEvent.senderTransactionId(),
			"계좌 이체",
			transferEvent.transactionTime(),
			TransactionType.TRANSFER,
			transferEvent.amount(),
			transferEvent.senderAccountId().toString(),
			transferEvent.receiverAccountId().toString(),
			"계좌 이체 처리"
		);

		ledgerRepository.saveJournalEntry(transferEntry);

		JournalLine debitLine = JournalLine.create(
			transferEntry.getId(),
			transferEvent.senderAccountId(),
			EntryType.DEBIT,
			transferEvent.amount()
		);

		JournalLine creditLine = JournalLine.create(
			transferEntry.getId(),
			transferEvent.receiverAccountId(),
			EntryType.CREDIT,
			transferEvent.amount()
		);

		journalLines.add(debitLine);
		journalLines.add(creditLine);

		ledgerRepository.bulkInsertJournalLines(journalLines);
	}

	@Async("ledgerExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleDeposit(DepositEvent event) {

		List<JournalLine> journalLines = new ArrayList<>();

		JournalEntry depositEntry = createJournalEntry(
			event.transactionId(),
			"계좌 입금",
			event.transactionTime(),
			TransactionType.DEPOSIT,
			event.amount(),
			bankCashAccountId.toString(),
			event.accountId().toString(),
			"현금 입금 처리"
		);

		ledgerRepository.saveJournalEntry(depositEntry);

		JournalLine customerLine = JournalLine.create(
			depositEntry.getId(),
			event.accountId(),
			EntryType.CREDIT,
			event.amount()
		);

		JournalLine bankLine = JournalLine.create(
			depositEntry.getId(),
			bankCashAccountId,
			EntryType.DEBIT,
			event.amount()
		);

		journalLines.add(customerLine);
		journalLines.add(bankLine);

		ledgerRepository.bulkInsertJournalLines(journalLines);
	}

	@Async("ledgerExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleWithdrawal(WithdrawalEvent event) {

		List<JournalLine> journalLines = new ArrayList<>();

		JournalEntry withdrawalEntry = createJournalEntry(
			event.transactionId(),
			"계좌 출금",
			event.transactionTime(),
			TransactionType.WITHDRAWAL,
			event.amount(),
			event.accountId().toString(),
			bankCashAccountId.toString(),
			"현금 출금 처리"
		);

		ledgerRepository.saveJournalEntry(withdrawalEntry);

		JournalLine customerLine = JournalLine.create(
			withdrawalEntry.getId(),
			event.accountId(),
			EntryType.DEBIT,
			event.amount()
		);

		JournalLine bankLine = JournalLine.create(
			withdrawalEntry.getId(),
			bankCashAccountId,
			EntryType.CREDIT,
			event.amount()
		);

		journalLines.add(customerLine);
		journalLines.add(bankLine);

		ledgerRepository.bulkInsertJournalLines(journalLines);
	}

	private JournalEntry createJournalEntry(
		Long transactionId,
		String summary,
		LocalDateTime eventTime,
		TransactionType transactionType,
		Long amount,
		String senderAccount,
		String recipientAccount,
		String additionalNotes
	) {
		JournalEntryInfo info = new JournalEntryInfo(
			summary,
			eventTime,
			amount,
			senderAccount,
			recipientAccount,
			additionalNotes
		);
		return JournalEntry.create(
			transactionId,
			ReconciliationStatus.PENDING,
			info,
			transactionType
		);
	}
}
