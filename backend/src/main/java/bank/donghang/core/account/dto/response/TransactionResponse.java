package bank.donghang.core.account.dto.response;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.dto.request.TransactionRequest;

public record TransactionResponse(
	String sendingAccountNumber,
	String receivingAccountNumber,
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
			sendingAccount.getAccountNumber(),
			receivingAccount.getAccountNumber(),
			sendingAccount.getAccountBalance(),
			request.amount(),
			transaction.getStatus(),
			transaction.getId()
		);
	}
}
