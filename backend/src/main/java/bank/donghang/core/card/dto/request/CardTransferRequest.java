package bank.donghang.core.card.dto.request;

import java.time.LocalDateTime;

public record CardTransferRequest(
		String cardNumber,
		String recipientAccountNumber,
		Long amount,
		String description,
		LocalDateTime sessionStartTime
) {
}
