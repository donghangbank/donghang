package bank.donghang.core.account.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.domain.repository.TransactionRepository;
import bank.donghang.core.account.dto.request.DepositRequest;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.request.WithdrawalRequest;
import bank.donghang.core.account.dto.response.DepositResponse;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.account.dto.response.WithdrawalResponse;
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
		waitTime = 3L,
		leaseTime = 3L
	)
	public TransactionResponse transferByAccount(TransactionRequest request) {
		Account sendingAccount = accountRepository.getAccount(request.sendingAccountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));
		Account receivingAccount = accountRepository.getAccount(request.receivingAccountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		validateBalance(request.amount(), sendingAccount);

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
	@SingleAccountLock(
		key = "#request.accountId",
		waitTime = 3L,
		leaseTime = 3L
	)
	public DepositResponse deposit(DepositRequest request) {
		Account account = accountRepository.getAccount(request.accountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		account.deposit(request.amount());

		Transaction transaction = Transaction.createTransaction(
			"입금",
			request.amount(),
			request.accountId(),
			TransactionType.DEPOSIT,
			TransactionStatus.COMPLETED
		);

		transactionRepository.saveTransaction(transaction);

		DepositResponse response = DepositResponse.of(
			account,
			transaction
		);

		return response;
	}

	@Transactional
	@SingleAccountLock(
		key = "#request.accountId",
		waitTime = 3L,
		leaseTime = 3L
	)
	public WithdrawalResponse withdraw(WithdrawalRequest request) {
		Account account = accountRepository.getAccount(request.accountId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		validateBalance(request.amount(), account);

		account.withdraw(request.amount());

		Transaction transaction = Transaction.createTransaction(
			"출금",
			request.amount(),
			request.accountId(),
			TransactionType.WITHDRAWAL,
			TransactionStatus.COMPLETED
		);

		transactionRepository.saveTransaction(transaction);

		WithdrawalResponse response = WithdrawalResponse.of(
			account,
			transaction
		);

		return response;
	}

	private void validateBalance(Long amount, Account account) {
		if (amount.compareTo(account.getAccountBalance()) > 0) {
			throw new BadRequestException(ErrorCode.NOT_ENOUGH_BALANCE);
		}
	}
}