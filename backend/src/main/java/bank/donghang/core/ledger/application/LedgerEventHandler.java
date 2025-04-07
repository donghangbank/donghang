package bank.donghang.core.ledger.application;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${DONGHANG_CASH_ACCOUNT_ID}")
	private Long bankCashAccountId;

	//	@Value("${DONGHANG_ASSET_ACCOUNT_ID}")
	//	private Long bankAssetAccountId;

	private final LedgerRepository ledgerRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleTransfer(TransferEvent transferEvent) {
		// 이체 거래는 1개의 JournalEntry에 2개의 JournalLine을 생성 (DEBIT + CREDIT)
		JournalEntry transferEntry = createJournalEntry(
			transferEvent.senderTransactionId(), // 메인 트랜잭션 ID 사용
			"계좌 이체",
			transferEvent.transactionTime(),
			TransactionType.TRANSFER,
			transferEvent.amount(),
			transferEvent.senderAccountId().toString(),
			transferEvent.receiverAccountId().toString(),
			"계좌 이체 처리"
		);

		ledgerRepository.saveJournalEntry(transferEntry);

		// 송금 측 (DEBIT)
		JournalLine debitLine = JournalLine.create(
			transferEntry.getId(),
			transferEvent.senderAccountId(),
			EntryType.DEBIT,
			transferEvent.amount()
		);

		// 입금 측 (CREDIT)
		JournalLine creditLine = JournalLine.create(
			transferEntry.getId(),
			transferEvent.receiverAccountId(),
			EntryType.CREDIT,
			transferEvent.amount()
		);

		ledgerRepository.saveJournalLine(debitLine);
		ledgerRepository.saveJournalLine(creditLine);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleDeposit(DepositEvent event) {
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

		ledgerRepository.saveJournalLine(customerLine);
		ledgerRepository.saveJournalLine(bankLine);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleWithdrawal(WithdrawalEvent event) {
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

		ledgerRepository.saveJournalLine(customerLine);
		ledgerRepository.saveJournalLine(bankLine);
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
			transactionType,
			amount,
			senderAccount,
			recipientAccount,
			additionalNotes
		);
		return JournalEntry.create(
			transactionId,
			ReconciliationStatus.PENDING,
			info
		);
	}
}