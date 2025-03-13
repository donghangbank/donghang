package bank.donghang.donghang_api.cardproduct.dto.request;

import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;

public record CardProductCreateRequest(
        String name,
        String description,
        CardProductType type,
        Long cardCompanyId
) {
}
