package bank.donghang.core.account.dto.response;

import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record AccountSummaryResponse(
	String bankName,
	@Mask(type = MaskingType.ACCOUNT_NUMBER)
	String accountNumber,
	AccountProductType accountProductType,
	Long balance
) {
}
