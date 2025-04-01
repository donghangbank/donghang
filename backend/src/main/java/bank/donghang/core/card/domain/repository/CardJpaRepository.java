package bank.donghang.core.card.domain.repository;

import bank.donghang.core.card.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardJpaRepository extends JpaRepository<Card, Long> {
}
