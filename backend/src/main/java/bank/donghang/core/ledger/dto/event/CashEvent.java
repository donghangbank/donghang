package bank.donghang.core.ledger.dto.event;

import java.time.LocalDateTime;

public record CashEvent(
	Long transactionId,
	Long accountId,
	Long amount,
	LocalDateTime transactionTime
) {
}
