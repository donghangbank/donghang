package bank.donghang.donghang_api.cardproduct.domain.repository;

import bank.donghang.donghang_api.cardproduct.domain.CardProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardProductJpaRepository extends JpaRepository<CardProduct, Long> {
}
