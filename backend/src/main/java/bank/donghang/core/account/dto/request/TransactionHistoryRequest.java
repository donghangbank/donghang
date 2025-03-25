package bank.donghang.core.account.dto.request;

public record TransactionHistoryRequest(
	String accountNumber,
	String password
) {
}
