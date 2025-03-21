package bank.donghang.core.account.application;

import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.domain.repository.TransactionRepository;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.common.exception.BadRequestException;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

	@InjectMocks
	private TransactionService transactionService;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@Test
	@DisplayName("계좌 잔액이 부족하면 이체를 실패한다.")
	void can_not_transfer_by_account_if_there_is_not_enough_balance() {

		Long sendingAccountId = 1L;
		Long receivingAccountId = 2L;
		String sendingAccountNumber = "1101101234567890";
		String receivingAccountNumber = "1101100987654321";
		Long sendingAccountBalance = 10L;
		Long receivingAccountBalance = 200L;
		Long amount = 100L;
		Long transactionId = 1L;

		var request = new TransactionRequest(
			sendingAccountNumber,
			receivingAccountNumber,
			amount,
			"테스트 이체",
			LocalDateTime.of(2025, 3, 20, 10, 0, 0)
		);

		Account sendingAccount = Account.builder()
			.accountId(sendingAccountId)
			.memberId(100L)
			.accountProductId(200L)
			.withdrawalAccountId(3L)
			.accountTypeCode("110")
			.branchCode("110")
			.accountNumber("1234567890")
			.password("password123")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(10000L)
			.singleTransferLimit(5000L)
			.accountBalance(sendingAccountBalance)
			.interestRate(3.5)
			.accountExpiryDate(new java.util.Date())
			.build();

		Account receivingAccount = Account.builder()
			.accountId(receivingAccountId)
			.memberId(100L)
			.accountProductId(200L)
			.withdrawalAccountId(5L)
			.accountTypeCode("110")
			.branchCode("110")
			.accountNumber("0987654321")
			.password("password123")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(10000L)
			.singleTransferLimit(5000L)
			.accountBalance(receivingAccountBalance)
			.interestRate(3.5)
			.accountExpiryDate(new java.util.Date())
			.build();

		given(accountRepository.findAccountByFullAccountNumber(sendingAccountNumber))
			.willReturn(Optional.of(sendingAccount));
		given(accountRepository.findAccountByFullAccountNumber(receivingAccountNumber))
			.willReturn(Optional.of(receivingAccount));

		org.junit.jupiter.api.Assertions.assertThrows(BadRequestException.class, () -> {
			transactionService.transferByAccount(request);
		});

		verify(accountRepository, times(1)).findAccountByFullAccountNumber(sendingAccountNumber);
		verify(accountRepository, times(1)).findAccountByFullAccountNumber(receivingAccountNumber);
	}
}
