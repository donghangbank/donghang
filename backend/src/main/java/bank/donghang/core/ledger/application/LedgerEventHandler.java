package bank.donghang.core.ledger.application;

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

	private final LedgerRepository ledgerRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleTransfer(TransferEvent transferEvent) {
		JournalEntryInfo withdrawalInfo = new JournalEntryInfo(
			"계좌 이체 출금",
			transferEvent.transactionTime(),
			TransactionType.TRANSFER,
			transferEvent.amount(),
			transferEvent.senderAccountId().toString(),
			transferEvent.receiverAccountId().toString(),
			"송금 처리"
		);

		JournalEntryInfo depositInfo = new JournalEntryInfo(
			"계좌 이체 입금",
			transferEvent.transactionTime(),
			TransactionType.TRANSFER,
			transferEvent.amount(),
			transferEvent.senderAccountId().toString(),
			transferEvent.receiverAccountId().toString(),
			"입금 처리"
		);

		JournalEntry withdrawalEntry = JournalEntry.create(
			transferEvent.senderTransactionId(),
			ReconciliationStatus.PENDING,
			withdrawalInfo
		);

		JournalEntry depositEntry = JournalEntry.create(
			transferEvent.receiverTransactionId(),
			ReconciliationStatus.PENDING,
			depositInfo
		);

		ledgerRepository.saveJournalEntry(withdrawalEntry);
		ledgerRepository.saveJournalEntry(depositEntry);

		JournalLine senderLine = JournalLine.create(
			withdrawalEntry.getId(),
			transferEvent.senderAccountId(),
			EntryType.CREDIT,
			transferEvent.amount()
		);

		JournalLine receiverLine = JournalLine.create(
			depositEntry.getId(),
			transferEvent.receiverAccountId(),
			EntryType.DEBIT,
			transferEvent.amount()
		);

		// TODO: 벌크 인서트 고민
		ledgerRepository.saveJournalLine(senderLine);
		ledgerRepository.saveJournalLine(receiverLine);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleDeposit(DepositEvent event) {

		// TODO: 실제 은행의 자산을 관리하는 은행 구현하기
		Long bankCashId = 1001L;
		Long bankAssetId = 1001L;

		JournalEntryInfo depositInfo = new JournalEntryInfo(
			"계좌 입금",
			event.transactionTime(),
			TransactionType.DEPOSIT,
			event.amount(),
			bankAssetId.toString(),
			event.accountId().toString(),
			"현금 입금 처리"
		);

		JournalEntry depositEntry = JournalEntry.create(
			event.transactionId(),
			ReconciliationStatus.PENDING,
			depositInfo
		);

		JournalLine senderLine = JournalLine.create(
			depositEntry.getId(),
			event.accountId(),
			EntryType.DEBIT,
			event.amount()
		);
		JournalLine bankAssetLine = JournalLine.create(
			depositEntry.getId(),
			bankAssetId,
			EntryType.CREDIT,
			event.amount()
		);
		JournalLine bankCashLine = JournalLine.create(
			depositEntry.getId(),
			bankCashId,
			EntryType.DEBIT,
			event.amount()
		);

		ledgerRepository.saveJournalEntry(depositEntry);
		ledgerRepository.saveJournalLine(senderLine);
		ledgerRepository.saveJournalLine(bankAssetLine);
		ledgerRepository.saveJournalLine(bankCashLine);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleWithdrawal(WithdrawalEvent event) {

		// TODO: 실제 은행의 자산을 관리하는 은행 구현하기
		Long bankCashId = 1001L;
		Long bankAssetId = 1001L;

		JournalEntryInfo depositInfo = new JournalEntryInfo(
			"계좌 입금",
			event.transactionTime(),
			TransactionType.DEPOSIT,
			event.amount(),
			bankAssetId.toString(),
			event.accountId().toString(),
			"현금 입금 처리"
		);

		JournalEntry depositEntry = JournalEntry.create(
			event.transactionId(),
			ReconciliationStatus.PENDING,
			depositInfo
		);

		JournalLine senderLine = JournalLine.create(
			depositEntry.getId(),
			event.accountId(),
			EntryType.CREDIT,
			event.amount()
		);
		JournalLine bankAssetLine = JournalLine.create(
			depositEntry.getId(),
			bankAssetId,
			EntryType.DEBIT,
			event.amount()
		);
		JournalLine bankCashLine = JournalLine.create(
			depositEntry.getId(),
			bankCashId,
			EntryType.CREDIT,
			event.amount()
		);

		ledgerRepository.saveJournalEntry(depositEntry);
		ledgerRepository.saveJournalLine(senderLine);
		ledgerRepository.saveJournalLine(bankAssetLine);
		ledgerRepository.saveJournalLine(bankCashLine);
	}
}
