package bank.donghang.core.account.dto.request;

import java.time.LocalDateTime;

public record TransactionRequest(
	String sendingAccountNumber,
	String receivingAccountNumber,
	Long amount,
	String description,
	LocalDateTime sessionStartTime
) {
}
