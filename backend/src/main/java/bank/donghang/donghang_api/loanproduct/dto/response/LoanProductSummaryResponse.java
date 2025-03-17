package bank.donghang.donghang_api.loanproduct.dto.response;

import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.domain.enums.RepaymentMethod;

public record LoanProductSummaryResponse(
        Long loanProductId,
        Long bankId,
        String loanProductName,
        String bankName,
        LoanType type,
        Integer minLoanBalance,
        Integer maxLoanBalance,
        Double interestRate,
        RepaymentMethod repaymentMethod
) {
}
