package bank.donghang.core.ledger.dto.event;

import java.time.LocalDateTime;

public record TransferEvent(
	Long senderTransactionId,
	Long receiverTransactionId,
	Long amount,
	Long senderAccountId,
	Long receiverAccountId,
	LocalDateTime transactionTime
) {
}