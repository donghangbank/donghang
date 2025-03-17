package bank.donghang.donghang_api.accountproduct.dto.response;

import bank.donghang.donghang_api.accountproduct.domain.AccountProduct;

public record AccountProductSummary(
        Long accountProductId,
        String accountProductName,
        Long bankId,
        Double interestRate,
        Long subscriptionPeriod,
        Long minSubscriptionBalance,
        Long maxSubscriptionBalance,
        String accountProductType,
        Integer accountProductTypeCode
) {
    public static AccountProductSummary from(AccountProduct accountProduct) {
        return new AccountProductSummary(
                accountProduct.getAccountProductId(),
                accountProduct.getAccountProductName(),
                accountProduct.getBankId(),
                accountProduct.getInterestRate(),
                accountProduct.getSubscriptionPeriod(),
                accountProduct.getMinSubscriptionBalance(),
                accountProduct.getMaxSubscriptionBalance(),
                accountProduct.getAccountProductType().name(),
                accountProduct.getAccountProductType().getCode()
        );
    }
}
