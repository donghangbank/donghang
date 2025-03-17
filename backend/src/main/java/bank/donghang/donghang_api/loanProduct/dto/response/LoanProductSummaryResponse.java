package bank.donghang.donghang_api.loanProduct.dto.response;

import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.domain.enums.RepaymentMethod;

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
