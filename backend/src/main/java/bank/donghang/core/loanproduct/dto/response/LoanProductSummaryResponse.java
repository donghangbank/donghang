package bank.donghang.core.loanproduct.dto.response;

import bank.donghang.core.loanproduct.domain.enums.LoanType;
import bank.donghang.core.loanproduct.domain.enums.RepaymentMethod;

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
