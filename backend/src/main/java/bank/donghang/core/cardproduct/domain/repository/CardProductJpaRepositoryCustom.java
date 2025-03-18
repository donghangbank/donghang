package bank.donghang.core.cardproduct.domain.repository;

import java.util.List;

import bank.donghang.core.cardproduct.domain.enums.CardProductType;
import bank.donghang.core.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.core.cardproduct.dto.response.CardProductSummaryResponse;

public interface CardProductJpaRepositoryCustom {

	CardProductDetailResponse findCardProductDetailById(Long id);

	List<CardProductSummaryResponse> findCardProductSummaries(
		CardProductType type,
		String cardCompanyName
	);
}
