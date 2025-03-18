package bank.donghang.core.loanproduct.dto.request;

import bank.donghang.core.common.enums.Period;
import bank.donghang.core.loanproduct.domain.enums.LoanType;
import bank.donghang.core.loanproduct.domain.enums.RepaymentMethod;

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
