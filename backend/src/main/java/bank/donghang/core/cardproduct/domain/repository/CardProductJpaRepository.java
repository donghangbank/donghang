package bank.donghang.core.cardproduct.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.cardproduct.domain.CardProduct;

public interface CardProductJpaRepository extends JpaRepository<CardProduct, Long>, CardProductJpaRepositoryCustom {
}
