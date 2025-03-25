package bank.donghang.core.account.dto.response;

import java.time.LocalDateTime;

import bank.donghang.core.account.domain.enums.TransactionType;

public record TransactionHistoryResponse(
	Long transactionId,
	LocalDateTime transactionTime,
	TransactionType type,
	String description,
	Long amount,
	Long balance
) {
}
