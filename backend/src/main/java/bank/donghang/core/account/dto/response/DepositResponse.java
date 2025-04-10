package bank.donghang.core.account.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record DepositResponse(
	@Mask(type = MaskingType.ACCOUNT_NUMBER)
	String accountNumber,
	Long amount,
	Long balance,
	TransactionType transactionType,
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	LocalDateTime transactionTime
) {
	public static DepositResponse of(
		Account account,
		Transaction transaction
	) {
		return new DepositResponse(
			account.getAccountTypeCode() + account.getBranchCode() + account.getAccountNumber(),
			transaction.getAmount(),
			account.getAccountBalance(),
			TransactionType.DEPOSIT,
			transaction.getSessionStartTime()
		);
	}
}
