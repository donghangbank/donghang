package bank.donghang.core.account.dto.request;

import java.time.LocalDateTime;

public record WithdrawalRequest(
	String accountNumber,
	Long amount,
	LocalDateTime sessionStartTime
) {
}
