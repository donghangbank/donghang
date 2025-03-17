package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static bank.donghang.donghang_api.loanProduct.domain.QLoanProduct.loanProduct;
import static bank.donghang.donghang_api.bank.domain.QBank.bank;

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
                        bank.name,
                        loanProduct.period,
                        loanProduct.type,
                        loanProduct.minLoanBalance,
                        loanProduct.maxLoanBalance,
                        loanProduct.interestRate,
                        loanProduct.description,
                        loanProduct.repaymentMethod
                ))
                .from(loanProduct)
                .leftJoin(bank)
                .on(loanProduct.bankId.eq(bank.id))
                .where(loanProduct.id.eq(loanProductId))
                .fetchOne();
    }
}
