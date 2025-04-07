package bank.donghang.core.ledger.dto.event;

import java.time.LocalDateTime;

public record DepositEvent(
	Long transactionId,
	Long accountId,
	Long amount,
	LocalDateTime transactionTime
) {
}
