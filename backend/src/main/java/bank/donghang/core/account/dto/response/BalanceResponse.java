package bank.donghang.core.account.dto.response;

public record BalanceResponse(
	String accountNumber,
	String bankName,
	Long balance
) {
}
