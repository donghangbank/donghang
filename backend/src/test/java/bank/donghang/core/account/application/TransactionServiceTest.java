package bank.donghang.core.account.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.account.domain.Account;
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

    private Account sendingAccount;
    private Account receivingAccount;

    @BeforeEach
    void setUp() {
        sendingAccount = mock(Account.class);
        receivingAccount = mock(Account.class);
    }

    @Test
    @DisplayName("동시에 여러 개의 송금 요청이 들어와도 정상적으로 처리된다.")
    void testConcurrentTransfers() throws InterruptedException {
        Long sendingAccountId = 1L;
        Long receivingAccountId = 2L;
        Long initialBalance = 10000L;
        Long transferAmount = 1000L;
        int numberOfThreads = 10;

        when(accountRepository.getAccount(sendingAccountId)).thenReturn(Optional.of(sendingAccount));
        when(accountRepository.getAccount(receivingAccountId)).thenReturn(Optional.of(receivingAccount));
        when(sendingAccount.getAccountBalance()).thenReturn(initialBalance);

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executor.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " - 송금 요청 시작");
                    TransactionRequest request = new TransactionRequest(sendingAccountId, receivingAccountId, transferAmount, "Test Transfer");
                    transactionService.transferByAccount(request);
                    System.out.println(Thread.currentThread().getName() + " - 송금 요청 완료");
                } catch (Exception e) {
                    System.err.println(Thread.currentThread().getName() + " - 오류 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await();
        executor.shutdown();

        verify(sendingAccount, atMost(numberOfThreads)).withdraw(transferAmount);
        verify(receivingAccount, atMost(numberOfThreads)).deposit(transferAmount);
    }

    @Test
    @DisplayName("잔액이 부족할 경우 송금이 실패해야 한다.")
    void testInsufficientBalance() {
        Long sendingAccountId = 1L;
        Long receivingAccountId = 2L;
        Long transferAmount = 20000L;

        when(accountRepository.getAccount(sendingAccountId)).thenReturn(Optional.of(sendingAccount));
        when(accountRepository.getAccount(receivingAccountId)).thenReturn(Optional.of(receivingAccount));
        when(sendingAccount.getAccountBalance()).thenReturn(5000L);

        TransactionRequest request = new TransactionRequest(sendingAccountId, receivingAccountId, transferAmount, "Test Transfer");

        BadRequestException exception = assertThrows(BadRequestException.class, () -> transactionService.transferByAccount(request));
        assertEquals(ErrorCode.NOT_ENOUGH_BALANCE.getCode(), exception.getCode());
    }
}
