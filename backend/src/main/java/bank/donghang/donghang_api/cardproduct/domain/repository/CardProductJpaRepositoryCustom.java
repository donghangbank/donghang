package bank.donghang.donghang_api.cardproduct.domain.repository;

import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductSummaryResponse;

import java.util.List;

public interface CardProductJpaRepositoryCustom {

    CardProductDetailResponse findCardProductDetailById(Long id);

    List<CardProductSummaryResponse> findCardProductSummaries(
            CardProductType type,
            String cardCompanyName
    );
}
