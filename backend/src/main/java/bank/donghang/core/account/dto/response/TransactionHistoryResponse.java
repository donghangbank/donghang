package bank.donghang.core.account.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import bank.donghang.core.account.domain.enums.TransactionType;

public record TransactionHistoryResponse(
	Long transactionId,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	LocalDateTime transactionTime,
	TransactionType type,
	String description,
	Long amount,
	Long balance
) {
}
