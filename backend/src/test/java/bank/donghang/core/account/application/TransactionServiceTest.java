package bank.donghang.core.account.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.domain.repository.TransactionRepository;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@InjectMocks
	private TransactionService transactionService;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@Test
	@DisplayName("분산 락이 없으면 계좌 이체 동시성 처리가 되지 않는다.")
	void test_concurrent_transfer_with_out_distributed_locks() throws InterruptedException {
		int numberOfThreads = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		Long sendingAccountId = 1L;
		Long receivingAccountId = 2L;
		Long initialBalance = 1000L;
		Long transferAmount = 1L;

		Account sendingAccount = Account.builder()
			.memberId(123L)
			.accountProductId(456L)
			.accountTypeCode("SAVINGS")
			.branchCode("001")
			.accountNumber("1234567890")
			.password("secret")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100000L)
			.singleTransferLimit(50000L)
			.accountBalance(initialBalance)
			.interestRate(1.5)
			.accountExpiryDate(new Date())
			.build();
		Account receivingAccount = Account.builder()
			.memberId(123L)
			.accountProductId(456L)
			.accountTypeCode("SAVINGS")
			.branchCode("001")
			.accountNumber("1234567890")
			.password("secret")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100000L)
			.singleTransferLimit(50000L)
			.accountBalance(initialBalance)
			.interestRate(1.5)
			.accountExpiryDate(new Date())
			.build();

		when(accountRepository.getAccount(sendingAccountId))
			.thenReturn(Optional.of(sendingAccount));
		when(accountRepository.getAccount(receivingAccountId))
			.thenReturn(Optional.of(receivingAccount));

		TransactionRequest request = new TransactionRequest(
			sendingAccountId,
			receivingAccountId,
			transferAmount,
			"테스트 이체"
		);

		for (int i = 0; i < numberOfThreads; i++) {
			executorService.submit(() -> {
				try {
					transactionService.transferByAccountTest(request);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		Long expectedTotalTransferAmount = transferAmount * numberOfThreads;
		Long expectedSendingAccountBalance = initialBalance - expectedTotalTransferAmount;
		Long expectedReceivingAccountBalance = initialBalance + expectedTotalTransferAmount;

		System.out.println(expectedTotalTransferAmount);
		System.out.println(
			"송금계좌 정상 : " + expectedSendingAccountBalance + " 수신계좌 정상 : " + expectedReceivingAccountBalance
				+ " 수신계좌 실제 : " + sendingAccount.getAccountBalance() + " 출금계좌 실제 : "
				+ receivingAccount.getAccountBalance());

		assertNotEquals(expectedSendingAccountBalance, sendingAccount.getAccountBalance());
		assertNotEquals(expectedReceivingAccountBalance, receivingAccount.getAccountBalance());

		verify(transactionRepository, times(numberOfThreads * 2)).saveTransaction(any(Transaction.class));
	}

	// @Test
	// @DisplayName("동시에 여러 개의 송금 요청이 들어와도 정상적으로 처리된다.")
	// void testConcurrentTransfers() throws InterruptedException {
	// 	Long sendingAccountId = 1L;
	// 	Long receivingAccountId = 2L;
	// 	Long initialBalance = 10000L;
	// 	Long transferAmount = 1000L;
	// 	int numberOfThreads = 10;
	//
	// 	when(accountRepository.getAccount(sendingAccountId)).thenReturn(Optional.of(sendingAccount));
	// 	when(accountRepository.getAccount(receivingAccountId)).thenReturn(Optional.of(receivingAccount));
	// 	when(sendingAccount.getAccountBalance()).thenReturn(initialBalance);
	//
	// 	ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
	// 	CountDownLatch latch = new CountDownLatch(numberOfThreads);
	//
	// 	for (int i = 0; i < numberOfThreads; i++) {
	// 		executor.execute(() -> {
	// 			try {
	// 				System.out.println(Thread.currentThread().getName() + " - 송금 요청 시작");
	// 				TransactionRequest request = new TransactionRequest(sendingAccountId, receivingAccountId,
	// 					transferAmount, "Test Transfer");
	// 				transactionService.transferByAccount(request);
	// 				System.out.println(Thread.currentThread().getName() + " - 송금 요청 완료");
	// 			} catch (Exception e) {
	// 				System.err.println(Thread.currentThread().getName() + " - 오류 발생: " + e.getMessage());
	// 			} finally {
	// 				latch.countDown();
	// 			}
	// 		});
	// 	}
	//
	// 	latch.await();
	// 	executor.shutdown();
	//
	// 	verify(sendingAccount, atMost(numberOfThreads)).withdraw(transferAmount);
	// 	verify(receivingAccount, atMost(numberOfThreads)).deposit(transferAmount);
	// }
	//
	// @Test
	// @DisplayName("잔액이 부족할 경우 송금이 실패해야 한다.")
	// void testInsufficientBalance() {
	// 	Long sendingAccountId = 1L;
	// 	Long receivingAccountId = 2L;
	// 	Long transferAmount = 20000L;
	//
	// 	when(accountRepository.getAccount(sendingAccountId)).thenReturn(Optional.of(sendingAccount));
	// 	when(accountRepository.getAccount(receivingAccountId)).thenReturn(Optional.of(receivingAccount));
	// 	when(sendingAccount.getAccountBalance()).thenReturn(5000L);
	//
	// 	TransactionRequest request = new TransactionRequest(sendingAccountId, receivingAccountId, transferAmount,
	// 		"Test Transfer");
	//
	// 	BadRequestException exception = assertThrows(BadRequestException.class,
	// 		() -> transactionService.transferByAccount(request));
	// 	assertEquals(ErrorCode.NOT_ENOUGH_BALANCE.getCode(), exception.getCode());
	// }
}
