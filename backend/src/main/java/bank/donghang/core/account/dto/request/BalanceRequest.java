package bank.donghang.core.account.dto.request;

public record BalanceRequest(
	String accountNumber,
	String password
) {
}
