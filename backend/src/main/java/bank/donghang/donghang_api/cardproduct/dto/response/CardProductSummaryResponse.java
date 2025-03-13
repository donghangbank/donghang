package bank.donghang.donghang_api.cardproduct.dto.response;

import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;

public record CardProductSummaryResponse(
        Long cardProductId,
        String cardProductName,
        String cardImageUrl,
        CardProductType cardProductType,
        String cardCompanyName,
        String cardCompanyLogoUrl
) {
}
