package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductSummaryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bank.donghang.donghang_api.cardcompany.domain.QCardCompany.cardCompany;
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

    @Override
    public List<LoanProductSummaryResponse> getLoanProductSummaries(LoanType loanType) {
        return queryFactory.select(
                Projections.constructor(
                        LoanProductSummaryResponse.class,
                        loanProduct.id,
                        loanProduct.bankId,
                        loanProduct.name,
                        bank.name,
                        loanProduct.type,
                        loanProduct.minLoanBalance,
                        loanProduct.maxLoanBalance,
                        loanProduct.interestRate,
                        loanProduct.repaymentMethod
                ))
                .from(loanProduct)
                .leftJoin(bank)
                .on(loanProduct.bankId.eq(bank.id))
                .where(eqLoanType(loanType))
                .fetch();
    }

    private BooleanExpression eqLoanType(LoanType loanType) {
        return (loanType != null)
                ? loanProduct.type.eq(loanType)
                : null;
    }
}
