package bank.donghang.core.account.dto.response;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionType;

public record WithdrawalResponse(
	Long accountId,
	Long amount,
	Long balance,
	TransactionType transactionType
) {
	public static WithdrawalResponse of(
		Account account,
		Transaction transaction
	) {
		return new WithdrawalResponse(
			account.getAccountId(),
			transaction.getAmount(),
			account.getAccountBalance(),
			TransactionType.WITHDRAWAL
		);
	}
}
