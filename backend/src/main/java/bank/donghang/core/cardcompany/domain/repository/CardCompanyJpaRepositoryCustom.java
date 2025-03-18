package bank.donghang.core.cardcompany.domain.repository;

import java.util.List;

import bank.donghang.core.cardcompany.dto.response.CardCompanySummaryResponse;

public interface CardCompanyJpaRepositoryCustom {

	List<CardCompanySummaryResponse> findAllCardCompanySummaries();
}
