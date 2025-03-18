package bank.donghang.core.account.dto.request;

public record TransactionRequest(
	Long sendingAccountId,
	Long receivingAccountId,
	Long amount,
	String description
) {
}
