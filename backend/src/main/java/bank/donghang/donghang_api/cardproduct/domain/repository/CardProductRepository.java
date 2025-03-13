package bank.donghang.donghang_api.cardproduct.domain.repository;

import bank.donghang.donghang_api.cardproduct.domain.CardProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardProductRepository{

    private final CardProductJpaRepository cardProductJpaRepository;

    public CardProduct save(CardProduct cardProduct) {
        return cardProductJpaRepository.save(cardProduct);
    }
}
