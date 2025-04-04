package bank.donghang.core.account.dto.response;

public record InstallmentPaymentFailedAccount(
	Long accountId,
	String reason
) {
}
