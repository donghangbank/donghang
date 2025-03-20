package bank.donghang.core.account.dto.request;

import java.time.LocalDateTime;

public record DepositRequest(
	Long accountId,
	Long amount,
	LocalDateTime sessionStartTime
) {
}
