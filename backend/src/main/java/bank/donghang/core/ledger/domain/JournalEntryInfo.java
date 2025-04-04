package bank.donghang.core.ledger.domain;

import java.time.LocalDateTime;

import bank.donghang.core.account.domain.enums.TransactionType;

public record JournalEntryInfo(
	String summary,
	LocalDateTime eventTime,
	TransactionType transactionType,
	Long amount,

	String senderAccount,
	String recipientAccount,
	String additionalNotes
) {
}
