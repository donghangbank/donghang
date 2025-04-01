package bank.donghang.core.card.domain.repository;

import bank.donghang.core.card.dto.response.CardPasswordResponse;

public interface CardJpaRepositoryCustom {

	CardPasswordResponse checkCardPassword(String cardNumber);
}
