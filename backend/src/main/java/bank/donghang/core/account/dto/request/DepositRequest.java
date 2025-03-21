package bank.donghang.core.account.dto.request;

import java.time.LocalDateTime;

public record DepositRequest(
	String accountNumber,
	Long amount,
	LocalDateTime sessionStartTime
) {
}
