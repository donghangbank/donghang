package bank.donghang.core.account.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.request.DepositRequest;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.request.WithdrawalRequest;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ActiveProfiles("test")
@TestPropertySource(locations = "file:${user.dir}/test.env")
@SpringBootTest
class ConcurrentTransactionTest {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private AccountRepository accountRepository;

	private static final int THREAD_COUNT = 100;
	private static final Long INITIAL_BALANCE = 100L;
	private static final Long DEPOSIT_AMOUNT = 1L;
	private static final Long WITHDRAW_AMOUNT = 1L;
	private static final Long TRANSFER_AMOUNT = 1L;

	private Long accountId1;
	private Long accountId2;

	private String accountNumber1;
	private String accountNumber2;

	@BeforeEach
	void setUp() {

		accountRepository.deleteAllAccounts();

		Account account1 = Account.builder()
			.memberId(1L)
			.accountProductId(101L)
			.accountTypeCode("110")
			.branchCode("110")
			.accountNumber("12345678")
			.password("1234")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(1_000_000L)
			.singleTransferLimit(500_000L)
			.accountBalance(INITIAL_BALANCE)
			.interestRate(2.5)
			.accountExpiryDate(LocalDate.now())
			.build();

		Account account2 = Account.builder()
			.memberId(2L)
			.accountProductId(102L)
			.accountTypeCode("110")
			.branchCode("110")
			.accountNumber("87654321")
			.password("1234")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(1_000_000L)
			.singleTransferLimit(500_000L)
			.accountBalance(INITIAL_BALANCE)
			.interestRate(2.5)
			.accountExpiryDate(LocalDate.now())
			.build();

		accountRepository.saveAccount(account1);
		accountRepository.saveAccount(account2);

		accountId1 = account1.getAccountId();
		accountId2 = account2.getAccountId();

		accountNumber1 = "11011012345678";
		accountNumber2 = "11011087654321";
	}

	@Test
	void can_transfer_by_account_at_concurrent_situation() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					TransactionRequest request = new TransactionRequest(
						accountNumber1,
						accountNumber2,
						TRANSFER_AMOUNT,
						"test",
						LocalDateTime.now()
					);
					transactionService.transferByAccount(request);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);

		Account account1 = accountRepository.findAccountByFullAccountNumber(accountNumber1)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));
		Account account2 = accountRepository.findAccountByFullAccountNumber(accountNumber2)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		assertEquals(INITIAL_BALANCE - (THREAD_COUNT * DEPOSIT_AMOUNT), account1.getAccountBalance());
	}

	@Test
	void can_deposit_by_account_at_concurrent_situation() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					DepositRequest request = new DepositRequest(
						accountNumber1,
						DEPOSIT_AMOUNT,
						LocalDateTime.now()
					);
					transactionService.deposit(request);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		Account account = accountRepository.findAccountByFullAccountNumber(accountNumber1)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		assertEquals(INITIAL_BALANCE + (THREAD_COUNT * DEPOSIT_AMOUNT), account.getAccountBalance());
	}

	@Test
	void can_withdrawal_by_account_at_concurrent_situation() throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					WithdrawalRequest request = new WithdrawalRequest(
						accountNumber1,
						WITHDRAW_AMOUNT,
						LocalDateTime.now()
					);
					transactionService.withdraw(request);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();

		Account account = accountRepository.findAccountByFullAccountNumber(accountNumber1)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		assertEquals(INITIAL_BALANCE - (THREAD_COUNT * DEPOSIT_AMOUNT), account.getAccountBalance());
	}
}
