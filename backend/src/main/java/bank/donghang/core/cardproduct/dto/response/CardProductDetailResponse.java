package bank.donghang.core.cardproduct.dto.response;

import bank.donghang.core.cardproduct.domain.enums.CardDuration;
import bank.donghang.core.cardproduct.domain.enums.CardProductType;

public record CardProductDetailResponse(
	Long cardProductId,
	String cardProductName,
	CardProductType cardProductType,
	String cardProductImageUrl,
	String description,
	String cardCompanyName,
	String cardCompanyLogoUrl,
	CardDuration duration
) {
}
