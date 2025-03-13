package bank.donghang.donghang_api.cardproduct.domain.repository;

import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductSummaryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bank.donghang.donghang_api.cardcompany.domain.QCardCompany.cardCompany;
import static bank.donghang.donghang_api.cardproduct.domain.QCardProduct.cardProduct;

@Repository
@RequiredArgsConstructor
public class CardProductJpaRepositoryCustomImpl implements CardProductJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public CardProductDetailResponse findCardProductDetailById(Long id) {
        return queryFactory.select(
                Projections.constructor(
                        CardProductDetailResponse.class,
                        cardProduct.id,
                        cardProduct.name,
                        cardProduct.type,
                        cardProduct.imageUrl,
                        cardProduct.description,
                        cardCompany.name,
                        cardCompany.logoUrl,
                        cardProduct.duration
                ))
                .from(cardProduct)
                .leftJoin(cardCompany)
                .on(cardProduct.cardCompanyId.eq(cardCompany.id))
                .where(cardProduct.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<CardProductSummaryResponse> findCardProductSummaries(
            CardProductType type,
            String cardCompanyName
    ) {
        return queryFactory.select(
                        Projections.constructor(
                                CardProductSummaryResponse.class,
                                cardProduct.id,
                                cardProduct.name,
                                cardProduct.imageUrl,
                                cardProduct.type,
                                cardCompany.name,
                                cardCompany.logoUrl
                        ))
                .from(cardProduct)
                .leftJoin(cardCompany)
                .on(cardProduct.cardCompanyId.eq(cardCompany.id))
                .where(
                        eqType(type),
                        eqCardCompanyName(cardCompanyName)
                )
                .fetch();
    }

    private BooleanExpression eqType(CardProductType type) {
        return type != null ? cardProduct.type.eq(type) : null;
    }

    private BooleanExpression eqCardCompanyName(String cardCompanyName) {
        return (cardCompanyName != null && !cardCompanyName.isEmpty())
                ? cardCompany.name.eq(cardCompanyName)
                : null;
    }
}
