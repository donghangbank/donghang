package bank.donghang.donghang_api.loanProduct.dto.request;

import bank.donghang.donghang_api.common.enums.Period;

public record LoanProductUpdateRequest(
        Long ratingId,
        String name,
        Period period,
        Integer minLoanBalance,
        Integer maxLoanBalance,
        Double interestRate,
        String description
) {
}
