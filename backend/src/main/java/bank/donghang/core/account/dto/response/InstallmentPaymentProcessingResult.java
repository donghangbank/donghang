package bank.donghang.core.account.dto.response;

import java.util.List;

public record InstallmentPaymentProcessingResult(
	int total,
	int success,
	List<InstallmentPaymentFailedAccount> failed
) {
}
