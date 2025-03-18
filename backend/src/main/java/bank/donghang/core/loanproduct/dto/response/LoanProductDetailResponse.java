package bank.donghang.core.loanproduct.dto.response;

import bank.donghang.core.common.enums.Period;
import bank.donghang.core.loanproduct.domain.enums.LoanType;
import bank.donghang.core.loanproduct.domain.enums.RepaymentMethod;

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
