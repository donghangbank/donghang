package bank.donghang.core.account.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.domain.repository.TransactionRepository;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.common.annotation.DistributedLock;
import bank.donghang.core.common.annotation.SingleAccountLock;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;

	@Transactional
	@DistributedLock(
			key = "'TRANSACTION_' + #request.sendingAccountId + '_' + #request.receivingAccountId",
			waitTime = 5L,
			leaseTime = 10L
	)
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

		Transaction recipientTransaction = Transaction.createTransaction(
				request.description(),
				request.amount(),
				receivingAccount.getAccountId(),
				TransactionType.DEPOSIT,
				TransactionStatus.COMPLETED
		);

		transactionRepository.saveTransaction(senderTransaction);
		transactionRepository.saveTransaction(recipientTransaction);

		return TransactionResponse.of(
				request,
				sendingAccount,
				receivingAccount,
				senderTransaction
		);
	}

	@Transactional
	@SingleAccountLock(key = "#accountId", waitTime = 5L, leaseTime = 10L)
	public void deposit(Long accountId, Long amount) {
		Account account = accountRepository.getAccount(accountId)
				.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		account.deposit(amount);

		Transaction transaction = Transaction.createTransaction(
				"Deposit",
				amount,
				accountId,
				TransactionType.DEPOSIT,
				TransactionStatus.COMPLETED
		);

		transactionRepository.saveTransaction(transaction);
	}

	@Transactional
	@SingleAccountLock(key = "#accountId", waitTime = 5L, leaseTime = 10L)
	public void withdraw(Long accountId, Long amount) {
		Account account = accountRepository.getAccount(accountId)
				.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		validateBalance(amount, account);

		account.withdraw(amount);

		Transaction transaction = Transaction.createTransaction(
				"Withdrawal",
				amount,
				accountId,
				TransactionType.WITHDRAWAL,
				TransactionStatus.COMPLETED
		);

		transactionRepository.saveTransaction(transaction);
	}

	private void validateBalance(TransactionRequest request, Account sendingAccount) {
		if (request.amount().compareTo(sendingAccount.getAccountBalance()) > 0) {
			throw new BadRequestException(ErrorCode.NOT_ENOUGH_BALANCE);
		}
	}

	private void validateBalance(Long amount, Account account) {
		if (amount.compareTo(account.getAccountBalance()) > 0) {
			throw new BadRequestException(ErrorCode.NOT_ENOUGH_BALANCE);
		}
	}
}