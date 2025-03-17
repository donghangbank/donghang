package bank.donghang.donghang_api.loanproduct.dto.request;

import bank.donghang.donghang_api.common.enums.Period;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.domain.enums.RepaymentMethod;

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
