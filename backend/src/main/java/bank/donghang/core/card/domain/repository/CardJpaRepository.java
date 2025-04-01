package bank.donghang.core.card.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.card.domain.Card;

public interface CardJpaRepository extends JpaRepository<Card, Long>, CardJpaRepositoryCustom {
	boolean existsByCardNumber(String cardNumber);
}
