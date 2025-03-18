package bank.donghang.core.account.dto.response;

import bank.donghang.donghang_api.account.domain.Account;
import bank.donghang.donghang_api.account.domain.Transaction;
import bank.donghang.donghang_api.account.domain.enums.TransactionStatus;
import bank.donghang.donghang_api.account.dto.request.TransactionRequest;

public record TransactionResponse(
	Long sendingAccountId,
	Long receivingAccountBalance,
	Long sendingAccountBalance,
	Long amount,
	TransactionStatus status,
	Long transactionId
) {
	public static TransactionResponse of(
		TransactionRequest request,
		Account sendingAccount,
		Account receivingAccount,
		Transaction transaction
	) {
		return new TransactionResponse(
			sendingAccount.getAccountId(),
			receivingAccount.getAccountId(),
			sendingAccount.getAccountBalance(),
			request.amount(),
			transaction.getStatus(),
			transaction.getId()
		);
	}
}
