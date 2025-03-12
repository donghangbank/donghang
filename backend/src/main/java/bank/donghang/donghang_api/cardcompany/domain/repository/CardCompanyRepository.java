package bank.donghang.donghang_api.cardcompany.domain.repository;

import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardCompanyRepository extends JpaRepository<CardCompany, Long> {
}
