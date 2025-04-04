package bank.donghang.core.accountproduct.dto.request;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;

public record AccountProductCreationRequest(
	String accountProductName,
	String accountProductDescription,
	Long bankId,
	Double interestRate,
	String rateDescription,
	Integer accountProductTypeCode,
	Integer subscriptionPeriod,
	Long minSubscriptionBalance,
	Long maxSubscriptionBalance
) {
	public AccountProduct toEntity() {
		return AccountProduct.builder()
			.accountProductName(accountProductName)
			.accountProductDescription(accountProductDescription)
			.bankId(bankId)
			.interestRate(interestRate)
			.rateDescription(rateDescription)
			.accountProductType(AccountProductType.fromCode(accountProductTypeCode))
			.subscriptionPeriod(subscriptionPeriod)
			.minSubscriptionBalance(minSubscriptionBalance)
			.maxSubscriptionBalance(maxSubscriptionBalance)
			.build();
	}
}
