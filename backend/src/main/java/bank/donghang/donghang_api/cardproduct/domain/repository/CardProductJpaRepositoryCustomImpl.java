package bank.donghang.donghang_api.cardproduct.domain.repository;

import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
