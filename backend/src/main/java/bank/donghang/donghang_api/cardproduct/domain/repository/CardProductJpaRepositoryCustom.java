package bank.donghang.donghang_api.cardproduct.domain.repository;

import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;

public interface CardProductJpaRepositoryCustom {

    CardProductDetailResponse findCardProductDetailById(Long id);
}
