package bank.donghang.donghang_api.cardproduct.domain.repository;

import bank.donghang.donghang_api.cardproduct.domain.CardProduct;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardProductRepository{

    private final CardProductJpaRepository cardProductJpaRepository;
    private final CardProductJpaRepositoryCustomImpl cardProductJpaRepositoryCustomImpl;

    public CardProduct save(CardProduct cardProduct) {
        return cardProductJpaRepository.save(cardProduct);
    }

    public CardProductDetailResponse findCardProductDetailById(Long id) {
        return cardProductJpaRepositoryCustomImpl.findCardProductDetailById(id);
    }

    public boolean existsCardProduct(Long id) {
        return cardProductJpaRepository.existsById(id);
    }
}
