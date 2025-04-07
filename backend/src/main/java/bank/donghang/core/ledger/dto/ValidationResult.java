package bank.donghang.core.ledger.dto;

import java.util.List;

public record ValidationResult(
	long totalDebit,
	long totalCredit,
	int successfulEntries,
	int failedEntries,
	List<ErrorDetail> errors,
	List<Long> completedEntries
) {
}
