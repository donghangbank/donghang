package bank.donghang.core.account.application;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.domain.repository.TransactionRepository;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.common.annotation.DistributedLock;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;

	@DistributedLock(key = "#request.sendingAccountId", waitTime = 0L)
	public TransactionResponse transferByAccount(TransactionRequest request) {

		Account sendingAccount = accountRepository.getAccount(request.sendingAccountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));
		Account receivingAccount = accountRepository.getAccount(request.receivingAccountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		validateBalance(request, sendingAccount);

		sendingAccount.withdraw(request.amount());
		receivingAccount.deposit(request.amount());

		Transaction senderTransaction = Transaction.createTransaction(
			request.description(),
			request.amount(),
			sendingAccount.getAccountId(),
			TransactionType.WITHDRAWAL,
			TransactionStatus.COMPLETED
		);

		Transaction receipentTransaction = Transaction.createTransaction(
			request.description(),
			request.amount(),
			receivingAccount.getAccountId(),
			TransactionType.DEPOSIT,
			TransactionStatus.COMPLETED
		);

		transactionRepository.saveTransaction(senderTransaction);
		transactionRepository.saveTransaction(receipentTransaction);

		TransactionResponse response = TransactionResponse.of(
			request,
			sendingAccount,
			receivingAccount,
			senderTransaction
		);

		return response;
	}

	public TransactionResponse transferByAccountTest(TransactionRequest request) {

		Account sendingAccount = accountRepository.getAccount(request.sendingAccountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));
		Account receivingAccount = accountRepository.getAccount(request.receivingAccountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		validateBalance(request, sendingAccount);

		sendingAccount.withdraw(request.amount());
		receivingAccount.deposit(request.amount());

		Transaction senderTransaction = Transaction.createTransaction(
			request.description(),
			request.amount(),
			sendingAccount.getAccountId(),
			TransactionType.WITHDRAWAL,
			TransactionStatus.COMPLETED
		);

		Transaction receipentTransaction = Transaction.createTransaction(
			request.description(),
			request.amount(),
			receivingAccount.getAccountId(),
			TransactionType.DEPOSIT,
			TransactionStatus.COMPLETED
		);

		transactionRepository.saveTransaction(senderTransaction);
		transactionRepository.saveTransaction(receipentTransaction);

		TransactionResponse response = TransactionResponse.of(
			request,
			sendingAccount,
			receivingAccount,
			senderTransaction
		);

		return response;
	}

	private static void validateBalance(TransactionRequest request, Account sendingAccount) {
		if (request.amount() > sendingAccount.getAccountBalance()) {
			throw new BadRequestException(ErrorCode.NOT_ENOUGH_BALANCE);
		}
	}
}
