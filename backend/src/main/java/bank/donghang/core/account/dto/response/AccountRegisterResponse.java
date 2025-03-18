package bank.donghang.core.account.dto.response;

import java.util.Date;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.accountproduct.domain.AccountProduct;

public record AccountRegisterResponse(
	String productName,
	String withdrawalAccountId,
	String accountNumber,
	Long accountBalance,
	Double interestDate,
	Date accountExpiryDate
) {
	public static AccountRegisterResponse from(Account account, AccountProduct product) {
		return new AccountRegisterResponse(
			product.getAccountProductName(),
			account.getWithdrawalAccountId(),
			account.getAccountTypeCode() + account.getBranchCode() + account.getAccountNumber(),
			account.getAccountBalance(),
			product.getInterestRate(),
			account.getAccountExpiryDate()
		);
	}
}
