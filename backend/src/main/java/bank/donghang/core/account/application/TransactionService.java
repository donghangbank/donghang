package bank.donghang.core.account.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.domain.repository.TransactionRepository;
import bank.donghang.core.account.dto.request.DepositRequest;
import bank.donghang.core.account.dto.request.TransactionHistoryRequest;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.request.WithdrawalRequest;
import bank.donghang.core.account.dto.response.DepositResponse;
import bank.donghang.core.account.dto.response.TransactionHistoryResponse;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.account.dto.response.WithdrawalResponse;
import bank.donghang.core.common.annotation.DistributedLock;
import bank.donghang.core.common.annotation.TransferDistributedLock;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;

	@TransferDistributedLock(
		key1 = "#request.sendingAccountNumber",
		key2 = "#request.receivingAccountNumber",
		waitTime = 1000L,
		leaseTime = 3000L
	)
	public TransactionResponse transferByAccount(TransactionRequest request) {

		Account sendingAccount = accountRepository.findAccountByFullAccountNumber(request.sendingAccountNumber())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));
		Account receivingAccount = accountRepository.findAccountByFullAccountNumber(request.receivingAccountNumber())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		// checkDuplicateRequest(
		// 	sendingAccount.getAccountId(),
		// 	request.amount(),
		// 	request.sessionStartTime(),
		// 	TransactionType.WITHDRAWAL
		// );
		//
		// checkDuplicateRequest(
		// 	receivingAccount.getAccountId(),
		// 	request.amount(),
		// 	request.sessionStartTime(),
		// 	TransactionType.DEPOSIT
		// );

		Transaction senderTransaction = transfer(sendingAccount, receivingAccount, request.description(),
			request.amount(), request.sessionStartTime());

		return TransactionResponse.of(
			request,
			sendingAccount,
			receivingAccount,
			senderTransaction
		);
	}

	@DistributedLock(
		key = "#request.accountNumber",
		waitTime = 100L
	)
	public DepositResponse deposit(DepositRequest request) {

		Account account = accountRepository.findAccountByFullAccountNumber(request.accountNumber())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		// checkDuplicateRequest(
		// 	account.getAccountId(),
		// 	request.amount(),
		// 	request.sessionStartTime(),
		// 	TransactionType.DEPOSIT
		// );

		account.deposit(request.amount());

		Transaction transaction = Transaction.createTransaction(
			"입금",
			request.amount(),
			account.getAccountBalance(),
			account.getAccountId(),
			TransactionType.DEPOSIT,
			TransactionStatus.COMPLETED,
			request.sessionStartTime()
		);

		transactionRepository.saveTransaction(transaction);

		DepositResponse response = DepositResponse.of(
			account,
			transaction
		);

		return response;
	}

	@DistributedLock(
		key = "#request.accountNumber",
		waitTime = 100L
	)
	public WithdrawalResponse withdraw(WithdrawalRequest request) {

		Account account = accountRepository.findAccountByFullAccountNumber(request.accountNumber())
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		// checkDuplicateRequest(
		// 	account.getAccountId(),
		// 	request.amount(),
		// 	request.sessionStartTime(),
		// 	TransactionType.WITHDRAWAL
		// );

		validateBalance(
			request.amount(),
			account
		);

		account.withdraw(request.amount());

		Transaction transaction = Transaction.createTransaction(
			"출금",
			request.amount(),
			account.getAccountId(),
			account.getAccountBalance(),
			TransactionType.WITHDRAWAL,
			TransactionStatus.COMPLETED,
			request.sessionStartTime()
		);

		transactionRepository.saveTransaction(transaction);

		WithdrawalResponse response = WithdrawalResponse.of(
			account,
			transaction
		);

		return response;
	}

	public PageInfo<TransactionHistoryResponse> getTransactionHistories(
		TransactionHistoryRequest request,
		String pageToken
	) {
		validateAccountExistenceAndPassword(request.accountNumber(), request.password());

		PageInfo<TransactionHistoryResponse> response = transactionRepository.getTransactionHistoryByFullAccountNumber(
			request.accountNumber(),
			pageToken
		);

		return response;
	}

	private void validateBalance(Long amount, Account account) {
		if (amount.compareTo(account.getAccountBalance()) > 0) {
			throw new BadRequestException(ErrorCode.NOT_ENOUGH_BALANCE);
		}
	}

	private void checkDuplicateRequest(
		Long accountId,
		Long amount,
		LocalDateTime sessionStartTime,
		TransactionType type
	) {
		if (transactionRepository.existsByAccountIdAndAmountAndTypeAndSessionStartTime(
			accountId,
			amount,
			sessionStartTime,
			type
		)) {
			throw new BadRequestException(ErrorCode.DUPLICATE_REQUEST);
		}
	}

	private void validateAccountExistenceAndPassword(
		String fullAccountNumber,
		String password
	) {
		Account account = accountRepository.findAccountByFullAccountNumber(fullAccountNumber)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		if (!account.getPassword().equals(password)) {
			throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
		}
	}

	public Transaction transfer(
		Account sendingAccount,
		Account receivingAccount,
		String description,
		Long amount,
		LocalDateTime sessionStartTime) {

		validateBalance(amount, sendingAccount);

		sendingAccount.withdraw(amount);
		receivingAccount.deposit(amount);

		Transaction senderTransaction = Transaction.createTransaction(
			description,
			amount,
			sendingAccount.getAccountBalance(),
			sendingAccount.getAccountId(),
			TransactionType.WITHDRAWAL,
			TransactionStatus.COMPLETED,
			sessionStartTime
		);

		Transaction recipientTransaction = Transaction.createTransaction(
			description,
			amount,
			receivingAccount.getAccountBalance(),
			receivingAccount.getAccountId(),
			TransactionType.DEPOSIT,
			TransactionStatus.COMPLETED,
			sessionStartTime
		);

		transactionRepository.saveTransaction(senderTransaction);
		transactionRepository.saveTransaction(recipientTransaction);

		return senderTransaction;
	}
}
