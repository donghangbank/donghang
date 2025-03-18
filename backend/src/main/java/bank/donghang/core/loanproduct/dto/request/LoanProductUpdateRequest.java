package bank.donghang.core.loanproduct.dto.request;

import bank.donghang.core.common.enums.Period;

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
