package bank.donghang.donghang_api.loanProduct.dto.response;

import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.domain.enums.RepaymentMethod;

import java.time.Period;

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
