package bank.donghang.donghang_api.cardproduct.dto.request;

import bank.donghang.donghang_api.cardproduct.domain.enums.CardDuration;
import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;

public record CardProductUpdateRequest(
        String name,
        String description,
        CardProductType type,
        CardDuration duration
) {
}
