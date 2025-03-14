package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static bank.donghang.donghang_api.loanProduct.domain.QLoanProduct.loanProduct;

@Repository
@RequiredArgsConstructor
public class LoanProductJpaRepositoryCustomImpl implements LoanProductJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public LoanProductDetailResponse getLoanProductDetailResponse(Long loanProductId) {
        return queryFactory.select(
                Projections.constructor(
                        LoanProductDetailResponse.class,
                        loanProduct.id,
                        loanProduct.bankId,
                        loanProduct.name,
                        /* TODO:bank구현 후 조인 */
                        bank.name,
                        bank.logoUrl,
                        loanProduct.period,
                        loanProduct.type,
                        loanProduct.minLoanBalance,
                        loanProduct.maxLoanBalance,
                        loanProduct.interestRate,
                        loanProduct.description,
                        loanProduct.repaymentMethod
                ))
                .from(loanProduct)
                .leftJoin(ban)
    }
}
