package bank.donghang.core.account.dto.request;

public record DepositRequest(
	Long accountId,
	Long amount
) {
}
