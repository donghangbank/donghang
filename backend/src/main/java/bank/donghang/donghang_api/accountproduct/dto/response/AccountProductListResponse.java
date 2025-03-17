package bank.donghang.donghang_api.accountproduct.dto.response;

import java.util.List;

public record AccountProductListResponse(
        List<AccountProductSummary> products
) {
    public static AccountProductListResponse from(List<AccountProductSummary> products) {
        return new AccountProductListResponse(products);
    }
}
