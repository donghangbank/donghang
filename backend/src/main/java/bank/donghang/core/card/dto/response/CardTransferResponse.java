package bank.donghang.core.card.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record CardTransferResponse(
		String sendingCardNumber,
		@Mask(type = MaskingType.ACCOUNT_NUMBER)
		String receivingAccountNumber,
		Long sendingAccountBalance,
		String recipientName,
		Long amount,
		TransactionStatus status,
		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
		LocalDateTime transferTime
) {
}
