package bank.donghang.core.card.dto.response;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

import java.time.LocalDateTime;

public record CardTransferResponse(
		String sendingCardNumber,
		@Mask(type = MaskingType.ACCOUNT_NUMBER)
		String receivingAccountNumber,
		Long sendingAccountBalance,
		String recipientName,
		Long amount,
		TransactionStatus status,
		LocalDateTime transferTime
) {
}
