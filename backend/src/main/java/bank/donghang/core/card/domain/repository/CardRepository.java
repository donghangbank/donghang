package bank.donghang.core.card.domain.repository;

import org.springframework.stereotype.Repository;

import bank.donghang.core.card.dto.response.CardPasswordResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardRepository {

	private final CardJpaRepository cardJpaRepository;
	private final CardJpaRepositoryCustomImpl cardJpaRepositoryCustomImpl;

	public CardPasswordResponse checkCardPassword(String cardNumber) {
		return cardJpaRepositoryCustomImpl.checkCardPassword(cardNumber);
	}

	public boolean existsByCardNumber(String cardNumber) {
		return cardJpaRepository.existsByCardNumber(cardNumber);
	}

	public Long findAccountIdByCardNumber(String cardNumber) {
		return cardJpaRepositoryCustomImpl.findAccountIdByCardNumber(cardNumber);
	}
}
