package bank.donghang.core.cardproduct.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bank.donghang.core.cardproduct.domain.CardProduct;
import bank.donghang.core.cardproduct.domain.enums.CardProductType;
import bank.donghang.core.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.core.cardproduct.dto.response.CardProductSummaryResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardProductRepository {

	private final CardProductJpaRepository cardProductJpaRepository;
	private final CardProductJpaRepositoryCustomImpl cardProductJpaRepositoryCustomImpl;

	public CardProduct save(CardProduct cardProduct) {
		return cardProductJpaRepository.save(cardProduct);
	}

	public CardProductDetailResponse findCardProductDetailById(Long id) {
		return cardProductJpaRepositoryCustomImpl.findCardProductDetailById(id);
	}

	public List<CardProductSummaryResponse> findCardProductSummaries(
		CardProductType type,
		String cardCompanyName
	) {
		return cardProductJpaRepositoryCustomImpl.findCardProductSummaries(
			type,
			cardCompanyName
		);
	}

	public boolean existsCardProduct(Long id) {
		return cardProductJpaRepository.existsById(id);
	}

	public Optional<CardProduct> findCardProductById(Long id) {
		return cardProductJpaRepository.findById(id);
	}

	public void deleteCardProduct(Long id) {
		cardProductJpaRepository.deleteById(id);
	}
}
