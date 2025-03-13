package bank.donghang.donghang_api.cardcompany.domain.repository;

import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardCompanyJpaRepository extends JpaRepository<CardCompany, Long>{

    Optional<CardCompany> findById(Long id);
}
