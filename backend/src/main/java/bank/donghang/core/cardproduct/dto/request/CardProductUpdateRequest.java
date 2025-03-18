package bank.donghang.core.cardproduct.dto.request;

import bank.donghang.core.cardproduct.domain.enums.CardDuration;
import bank.donghang.core.cardproduct.domain.enums.CardProductType;

public record CardProductUpdateRequest(
	String name,
	String description,
	CardProductType type,
	CardDuration duration
) {
}
