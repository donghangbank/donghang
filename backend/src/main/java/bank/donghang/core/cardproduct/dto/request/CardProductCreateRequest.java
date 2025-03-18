package bank.donghang.core.cardproduct.dto.request;

import bank.donghang.core.cardproduct.domain.enums.CardDuration;
import bank.donghang.core.cardproduct.domain.enums.CardProductType;

public record CardProductCreateRequest(
	String name,
	String description,
	CardProductType type,
	Long cardCompanyId,
	CardDuration duration
) {
}
