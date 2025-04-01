package bank.donghang.core.ledger.dto.query;

import java.time.LocalDateTime;

public record TransactionSumDay(
	Long sum,
	LocalDateTime ledgerTime
) {
}
