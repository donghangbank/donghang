package bank.donghang.donghang_api.loanProduct.dto.request;

import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.domain.enums.RepaymentMethod;

import java.time.Period;

public record LoanProductCreateRequest(
        Long bankId,
        Long ratingId,
        String name,
        Period period,
        LoanType type,
        Integer minLoanBalance,
        Integer maxLoanBalance,
        Double interestRate,
        String description,
        RepaymentMethod repaymentMethod
) {
}
