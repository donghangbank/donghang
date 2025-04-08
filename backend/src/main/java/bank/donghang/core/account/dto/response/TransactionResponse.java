package bank.donghang.core.account.dto.response;

import java.time.LocalDateTime;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record TransactionResponse(
	String sendingAccountNumber,
	// @Mask(type = MaskingType.ACCOUNT_NUMBER)
	String receivingAccountNumber,
	Long sendingAccountBalance,
	String recipientName,
	Long amount,
	TransactionStatus status,
	Long transactionId,
	LocalDateTime transactionTime
) {
	public static TransactionResponse of(
		TransactionRequest request,
		Account sendingAccount,
		Account receivingAccount,
		Transaction transaction,
		String recipientName
	) {
		return new TransactionResponse(
			sendingAccount.getAccountTypeCode()
				+ sendingAccount.getBranchCode()
				+ sendingAccount.getAccountNumber(),
			receivingAccount.getAccountTypeCode()
				+ receivingAccount.getBranchCode()
				+ receivingAccount.getAccountNumber(),
			sendingAccount.getAccountBalance(),
			recipientName,
			request.amount(),
			transaction.getStatus(),
			transaction.getId(),
			transaction.getCreatedAt()
		);
	}
}
