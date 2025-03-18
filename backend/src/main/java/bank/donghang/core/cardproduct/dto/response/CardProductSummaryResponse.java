package bank.donghang.core.cardproduct.dto.response;

import bank.donghang.core.cardproduct.domain.enums.CardProductType;

public record CardProductSummaryResponse(
	Long cardProductId,
	String cardProductName,
	String cardImageUrl,
	CardProductType cardProductType,
	String cardCompanyName,
	String cardCompanyLogoUrl
) {
}
