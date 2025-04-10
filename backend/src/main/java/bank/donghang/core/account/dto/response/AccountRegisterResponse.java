package bank.donghang.core.account.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;


public record AccountRegisterResponse(
	String productName,
	@Mask(type = MaskingType.ACCOUNT_NUMBER)
	String withdrawalAccountNumber,
	@Mask(type = MaskingType.ACCOUNT_NUMBER)
	String payoutAccountNumber,
	String accountNumber,
	Long accountBalance,
	Double interestRate,
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	LocalDate accountExpiryDate,
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
	LocalDate nextInstallmentScheduleDate
) {
	public static AccountRegisterResponse from(
		Account account,
		AccountProduct product,
		LocalDate nextInstallmentScheduleDate,
		String withdrawalAccountNumber,
		String payoutAccountNumber) {
		return new AccountRegisterResponse(
			product.getAccountProductName(),
			withdrawalAccountNumber,
			payoutAccountNumber,
			account.getAccountTypeCode() + account.getBranchCode() + account.getAccountNumber(),
			account.getAccountBalance(),
			product.getInterestRate(),
			account.getAccountExpiryDate(),
			nextInstallmentScheduleDate
		);
	}
}
