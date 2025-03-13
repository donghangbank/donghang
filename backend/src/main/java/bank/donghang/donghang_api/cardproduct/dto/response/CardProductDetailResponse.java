package bank.donghang.donghang_api.cardproduct.dto.response;

import bank.donghang.donghang_api.cardproduct.domain.enums.CardDuration;
import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;

public record CardProductDetailResponse(
        Long cardProductId,
        String cardProductName,
        CardProductType cardProductType,
        String description,
        String cardCompanyName,
        String cardCompanyLogoUrl,
        CardDuration duration
) {
}
