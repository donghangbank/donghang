package bank.donghang.donghang_api.cardcompany.domain.repository;

import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;

import java.util.List;

public interface CardCompanyJpaRepositoryCustom {

    List<CardCompanySummaryResponse> findAllCardCompanySummaries();
}
