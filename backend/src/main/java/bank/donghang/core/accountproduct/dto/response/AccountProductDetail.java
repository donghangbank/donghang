package bank.donghang.core.accountproduct.dto.response;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.bank.domain.Bank;

public record AccountProductDetail(
	Long productId,
	String productName,
	String productDescription,
	Long bankId,
	String bankName,
	String bankLogoUrl,
	Double interestRate,
	String rateDescription,
	String productTypeName,
	Integer subscriptionPeriod,
	Long minSubscriptionBalance,
	Long maxSubscriptionBalance
) {
	public static AccountProductDetail from(AccountProduct accountProduct, Bank bank) {
		return new AccountProductDetail(
			accountProduct.getAccountProductId(),
			accountProduct.getAccountProductName(),
			accountProduct.getAccountProductDescription(),
			accountProduct.getBankId(),
			bank.getName(),
			bank.getLogoUrl(),
			accountProduct.getInterestRate(),
			accountProduct.getRateDescription(),
			accountProduct.getAccountProductType().name(),
			accountProduct.getSubscriptionPeriod(),
			accountProduct.getMinSubscriptionBalance(),
			accountProduct.getMaxSubscriptionBalance()
		);
	}
}
