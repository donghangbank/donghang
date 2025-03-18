package bank.donghang.donghang_api.account.dto.request;

public record TransactionRequest(
	Long sendingAccountId,
	Long receivingAccountId,
	Long amount,
	String description
) {
}
