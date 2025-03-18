package bank.donghang.donghang_api.account.dto.response;

import bank.donghang.donghang_api.account.domain.Account;
import bank.donghang.donghang_api.accountproduct.domain.AccountProduct;

import java.util.Date;

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
