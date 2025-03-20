package bank.donghang.core.account.dto.request;

import java.time.LocalDateTime;

public record TransactionRequest(
	Long sendingAccountId,
	Long receivingAccountId,
	Long amount,
	String description,
	LocalDateTime sessionStartTime
) {
}
