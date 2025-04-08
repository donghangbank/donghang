package bank.donghang.core.account.application;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
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
import bank.donghang.core.common.annotation.MaskApply;
import bank.donghang.core.common.annotation.TransferDistributedLock;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import bank.donghang.core.ledger.dto.event.DepositEvent;
import bank.donghang.core.ledger.dto.event.TransferEvent;
import bank.donghang.core.ledger.dto.event.WithdrawalEvent;
import bank.donghang.core.member.domain.Member;
import bank.donghang.core.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;
	private final ApplicationEventPublisher eventPublisher;

	@TransferDistributedLock(
		key1 = "#request.sendingAccountNumber",
		key2 = "#request.receivingAccountNumber",
		waitTime = 15L,
		leaseTime = 15L
	)
	// @MaskApply(typeValue = TransactionResponse.class)
	public TransactionResponse transferByAccount(TransactionRequest request) {

		if (request.sendingAccountNumber().equals(request.receivingAccountNumber())) {
			throw new BadRequestException(ErrorCode.SAME_ACCOUNT_TRANSFER);
		}

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

		Transaction senderTransaction = transfer(
			sendingAccount,
			receivingAccount,
			request.description(),
			request.amount(),
			request.sessionStartTime()
		);

		Long ownerId = receivingAccount.getMemberId();

		Member recipient = memberRepository.findById(ownerId)
			.orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

		return TransactionResponse.of(
			request,
			sendingAccount,
			receivingAccount,
			senderTransaction,
			recipient.getName()
		);
	}

	@DistributedLock(
		key = "#request.accountNumber"
	)
	@MaskApply(typeValue = DepositResponse.class)
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

		eventPublisher.publishEvent(
			new DepositEvent(
				transaction.getId(),
				account.getAccountId(),
				request.amount(),
				request.sessionStartTime()
			)
		);

		DepositResponse response = DepositResponse.of(
			account,
			transaction
		);

		return response;
	}

	@DistributedLock(
		key = "#request.accountNumber"
	)
	@MaskApply(typeValue = WithdrawalResponse.class)
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

		eventPublisher.publishEvent(
			new WithdrawalEvent(
				transaction.getId(),
				account.getAccountId(),
				request.amount(),
				request.sessionStartTime()
			)
		);

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
		validateAccountExistenceAndPassword(
			request.accountNumber(),
			request.password()
		);

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
		LocalDateTime sessionStartTime
	) {

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

		eventPublisher.publishEvent(
			new TransferEvent(
				senderTransaction.getId(),
				recipientTransaction.getId(),
				amount,
				sendingAccount.getAccountId(),
				receivingAccount.getAccountId(),
				sessionStartTime
			)
		);

		return senderTransaction;
	}
}
