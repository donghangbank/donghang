package bank.donghang.core.card.domain.repository;

import bank.donghang.core.card.dto.response.CardPasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepository {

    private final CardJpaRepository cardJpaRepository;

    public CardPasswordResponse checkCardPassword(String cardNumber){
        return cardJpaRepository.checkCardPassword(cardNumber);
    }

    public boolean existsByCardNumber(String cardNumber) {
        return cardJpaRepository.existsByCardNumber(cardNumber);
    }
}
