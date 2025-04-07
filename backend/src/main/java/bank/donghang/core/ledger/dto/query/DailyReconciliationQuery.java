package bank.donghang.core.ledger.dto.query;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.ledger.domain.enums.EntryType;

import java.time.LocalDateTime;

public record DailyReconciliationQuery(
	Long transactionId,
	TransactionType transactionType,
	TransactionStatus transactionStatus,
	Long journalEntryId,
	Long journalLineId,
	Long amount,
	Long accountId,
	EntryType entryType,
	LocalDateTime transactionTime
) {
}
