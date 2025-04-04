package bank.donghang.core.ledger.dto.query;

import bank.donghang.core.ledger.domain.enums.EntryType;

public record DailyReconciliationQuery(
	Long transactionId,
	Long journalEntryId,
	Long journalLineId,
	Long amount,
	Long accountId,
	EntryType entryType
) {
}
