package bank.donghang.core.cardcompany.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.cardcompany.domain.CardCompany;

public interface CardCompanyJpaRepository extends JpaRepository<CardCompany, Long> {

	Optional<CardCompany> findById(Long id);
}
