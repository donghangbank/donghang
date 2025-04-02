package bank.donghang.core.account.dto.response;

import java.time.LocalDateTime;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record WithdrawalResponse(
	@Mask(type = MaskingType.ACCOUNT_NUMBER)
	String accountNumber,
	Long amount,
	Long balance,
	TransactionType transactionType,
	LocalDateTime transactionTime
) {
	public static WithdrawalResponse of(
		Account account,
		Transaction transaction
	) {
		return new WithdrawalResponse(
			account.getAccountTypeCode() + account.getBranchCode() + account.getAccountNumber(),
			transaction.getAmount(),
			account.getAccountBalance(),
			TransactionType.WITHDRAWAL,
			transaction.getSessionStartTime()
		);
	}
}
