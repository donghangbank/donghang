package bank.donghang.donghang_api.cardcompany.domain.repository;

import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bank.donghang.donghang_api.cardcompany.domain.QCardCompany.cardCompany;

@Repository
@RequiredArgsConstructor
public class CardCompanyJpaRepositoryCustomImpl implements CardCompanyJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CardCompanySummaryResponse> findAllCardCompanySummaries() {
        return queryFactory.select(
                Projections.constructor(
                        CardCompanySummaryResponse.class,
                        cardCompany.id,
                        cardCompany.name,
                        cardCompany.logoUrl
                ))
                .from(cardCompany)
                .fetch();
    }
}
