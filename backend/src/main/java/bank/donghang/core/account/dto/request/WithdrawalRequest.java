package bank.donghang.core.account.dto.request;

public record WithdrawalRequest(
	Long accountId,
	Long amount
) {
}
