package bank.donghang.core.accountproduct.dto.response;

import bank.donghang.core.accountproduct.domain.AccountProduct;

public record AccountProductDetail(
	Long productId,
	String productName,
	String productDescription,
	Long bankId,
	Double interestRate,
	String rateDescription,
	String productTypeName,
	Integer productTypeCode,
	Integer subscriptionPeriod,
	Long minSubscriptionBalance,
	Long maxSubscriptionBalance
) {
	public static AccountProductDetail from(AccountProduct accountProduct) {
		return new AccountProductDetail(
			accountProduct.getAccountProductId(),
			accountProduct.getAccountProductName(),
			accountProduct.getAccountProductDescription(),
			accountProduct.getBankId(),
			accountProduct.getInterestRate(),
			accountProduct.getRateDescription(),
			accountProduct.getAccountProductType().name(),
			accountProduct.getAccountProductType().getCode(),
			accountProduct.getSubscriptionPeriod(),
			accountProduct.getMinSubscriptionBalance(),
			accountProduct.getMaxSubscriptionBalance()
		);
	}
}
