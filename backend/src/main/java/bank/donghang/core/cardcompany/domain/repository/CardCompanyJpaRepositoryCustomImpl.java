package bank.donghang.core.cardcompany.domain.repository;

import static bank.donghang.core.cardcompany.domain.QCardCompany.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.donghang.core.cardcompany.dto.response.CardCompanySummaryResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardCompanyJpaRepositoryCustomImpl implements CardCompanyJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<CardCompanySummaryResponse> findAllCardCompanySummaries() {
		return queryFactory.select(
			Projections.constructor(CardCompanySummaryResponse.class, cardCompany.id, cardCompany.name,
				cardCompany.logoUrl)).from(cardCompany).fetch();
	}
}
