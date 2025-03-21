package bank.donghang.core.account.dto.response;

import java.util.Date;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.accountproduct.domain.AccountProduct;

public record AccountRegisterResponse(
	String productName,
	String withdrawalAccountNumber,
	String payoutAccountNumber,
	String accountNumber,
	Long accountBalance,
	Double interestDate,
	Date accountExpiryDate
) {
	public static AccountRegisterResponse from(Account account, AccountProduct product, String withdrawalAccountNumber,
		String payoutAccountNumber) {
		return new AccountRegisterResponse(
			product.getAccountProductName(),
			withdrawalAccountNumber,
			payoutAccountNumber,
			account.getAccountTypeCode() + account.getBranchCode() + account.getAccountNumber(),
			account.getAccountBalance(),
			product.getInterestRate(),
			account.getAccountExpiryDate()
		);
	}
}
