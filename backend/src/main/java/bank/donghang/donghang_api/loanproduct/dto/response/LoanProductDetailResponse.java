package bank.donghang.donghang_api.loanproduct.dto.response;

import bank.donghang.donghang_api.common.enums.Period;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.domain.enums.RepaymentMethod;

public record LoanProductDetailResponse(
        Long loanProductId,
        Long bankId,
        String loanProductName,
        String bankName,
        Period period,
        LoanType type,
        Integer minLoanBalance,
        Integer maxLoanBalance,
        Double interestRate,
        String description,
        RepaymentMethod repaymentMethod
) {
}
