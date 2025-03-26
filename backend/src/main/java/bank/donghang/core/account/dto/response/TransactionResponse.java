package bank.donghang.core.account.dto.response;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record TransactionResponse(
	String sendingAccountNumber,
	@Mask(type = MaskingType.ACCOUNT_NUMBER)
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
			sendingAccount.getAccountTypeCode() + sendingAccount.getBranchCode() + sendingAccount.getAccountNumber(),
			receivingAccount.getAccountTypeCode() + receivingAccount.getBranchCode()
				+ receivingAccount.getAccountNumber(),
			sendingAccount.getAccountBalance(),
			request.amount(),
			transaction.getStatus(),
			transaction.getId()
		);
	}
}
