package bank.donghang.core.ledger.dto;

import java.util.List;

public record ReconciliationResult(
		long totalDebit,
		long totalCredit,
		int successfulEntries,
		int failedEntries,
		List<ErrorDetail> errors
) {}
