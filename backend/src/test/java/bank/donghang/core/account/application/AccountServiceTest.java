package bank.donghang.core.account.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.InstallmentSchedule;
import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.TransferInfo;
import bank.donghang.core.account.dto.request.BalanceRequest;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.request.MyAccountsRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.account.dto.response.AccountSummaryResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.account.dto.response.InstallmentPaymentProcessingResult;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@InjectMocks
	private AccountService accountService;

	@Mock
	private TransferFacade transferFacade;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AccountProductRepository accountProductRepository;

	@Test
	@DisplayName("사용자는 본인의 계좌 목록을 조회할 수 있다.")
	void getMyAccounts() {
		Long memberId = 1L;
		String pageToken = "123"; // 예: 마지막 계좌의 accountId가 123
		Long cursor = Long.parseLong(pageToken);

		AccountSummaryResponse summary = new AccountSummaryResponse(
			"TestBank",
			"****1234",
			AccountProductType.DEMAND,
			1000L
		);
		PageInfo<AccountSummaryResponse> expected = PageInfo.of(
			"456",
			List.of(summary),
			true
		);

		when(accountRepository.getMyAccounts(memberId, cursor)).thenReturn(expected);

		PageInfo<AccountSummaryResponse> result = accountService.getMyAccounts(new MyAccountsRequest(memberId),
			pageToken);
		assertNotNull(result);
		assertEquals(expected, result);
		verify(accountRepository).getMyAccounts(memberId, cursor);
	}

	@Test
	@DisplayName("사용자는 자유입출금 계좌를 생성할 수 있다.")
	void createDemandAccount() {
		Long productId = 1L;
		DemandAccountRegisterRequest request = mock(DemandAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);

		AccountProduct accountProduct = mock(AccountProduct.class);
		doReturn(0.05).when(accountProduct).getInterestRate();
		when(accountProductRepository.getAccountProductById(productId))
			.thenReturn(accountProduct);
		when(accountProductRepository.existsAccountProductById(productId))
			.thenReturn(true);

		String nextAccountNumber = "000001";
		when(accountRepository.getNextAccountNumber(
			"100",
			"001"
			)
		)
			.thenReturn(nextAccountNumber);

		Account account = mock(Account.class);
		when(request.toEntity(nextAccountNumber, accountProduct.getInterestRate()))
			.thenReturn(account);

		when(accountRepository.saveAccount(account)).thenReturn(account);

		AccountRegisterResponse response = accountService.createDemandAccount(request);
		assertNotNull(response);
		verify(accountProductRepository).getAccountProductById(productId);
		verify(accountRepository).getNextAccountNumber(
			"100",
			"001"
		);
		verify(accountRepository).saveAccount(account);
	}

	@Test
	@DisplayName("존재하지 않는 계좌 상품으로 계좌 생성을 시도하면 에러가 발생한다. (Demand 계좌)")
	void createDemandAccount_shouldThrowExceptionWhenAccountProductNotExist() {
		Long productId = 2000L;
		DemandAccountRegisterRequest request = new DemandAccountRegisterRequest(
			1L,
			productId,
			"1234",
			true
		);

		when(accountProductRepository.existsAccountProductById(productId))
			.thenThrow(new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND));

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> accountService.createDemandAccount(request));
		assertEquals(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND.getCode(), exception.getCode());
		verify(accountProductRepository).existsAccountProductById(productId);
	}

	@Test
	@DisplayName("사용자는 예금 상품에 가입할 수 있다.")
	void createDepositAccount_success() {
		Long productId = 2L;
		Long memberId = 100L;
		String withdrawalAccountNumber = "100001000002";
		String payoutAccountNumber = "100001000003";

		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);
		when(request.initDepositAmount()).thenReturn(5000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProduct.getInterestRate()).thenReturn(0.5);
		when(accountProduct.getSubscriptionPeriod()).thenReturn(12);

		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		Account payoutAccount = mock(Account.class);

		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber))
			.thenReturn(Optional.of(withdrawalAccount));
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber))
			.thenReturn(Optional.of(payoutAccount));

		doNothing().when(withdrawalAccount).verifyWithdrawalAccount(memberId, 5000L);
		doNothing().when(payoutAccount).verifyPayoutAccount(memberId);

		String newDepositAccountNumber = "200001000010";
		when(accountRepository.getNextAccountNumber(
			"200",
			"001"
			)
		).thenReturn(newDepositAccountNumber);

		when(withdrawalAccount.getAccountId()).thenReturn(101L);
		when(payoutAccount.getAccountId()).thenReturn(202L);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 12);
		LocalDate expiryDate = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Account depositAccount = mock(Account.class);
		when(request.toEntity(
			eq(newDepositAccountNumber),
			eq(0.5),
			eq(101L),
			eq(202L),
			eq(0L),
			any(LocalDate.class)))
			.thenReturn(depositAccount);

		doNothing().when(transferFacade).transfer(any());

		when(accountRepository.saveAccount(depositAccount)).thenReturn(depositAccount);

		AccountRegisterResponse response = accountService.createDepositAccount(request);
		assertNotNull(response);
		verify(transferFacade).transfer(any());
	}

	@Test
	@DisplayName("예금 가입 시 존재하지 않는 출금 계좌를 입력하면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenWithdrawalAccountNotExist() {
		Long productId = 2L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		when(accountRepository.findAccountByFullAccountNumber("100001000002")).thenReturn(Optional.empty());
		when(accountRepository.findAccountByFullAccountNumber("100001000003")).thenReturn(
			Optional.of(mock(Account.class)));

		BadRequestException ex = assertThrows(BadRequestException.class,
			() -> accountService.createDepositAccount(request));
		assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("예금 가입 시 본인 소유가 아닌 출금 계좌를 입력하면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenWithdrawalAccountNotOwned() {
		Long productId = 2L;
		Long memberId = 100L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");
		when(request.initDepositAmount()).thenReturn(5000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002")).thenReturn(
			Optional.of(withdrawalAccount));
		lenient().when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(mock(Account.class)));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_NOT_OWNED)).when(withdrawalAccount)
			.verifyWithdrawalAccount(memberId, 5000L);

		BadRequestException ex = assertThrows(BadRequestException.class,
			() -> accountService.createDepositAccount(request));
		assertEquals(ErrorCode.WITHDRAWAL_ACCOUNT_NOT_OWNED.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("예금 가입 시 출금 계좌가 활성화 상태가 아니면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenWithdrawalAccountNotActive() {
		Long productId = 2L;
		Long memberId = 100L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");
		when(request.initDepositAmount()).thenReturn(5000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002")).thenReturn(
			Optional.of(withdrawalAccount));
		lenient().when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(mock(Account.class)));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_IS_NOT_ACTIVE)).when(withdrawalAccount)
			.verifyWithdrawalAccount(memberId, 5000L);

		BadRequestException ex = assertThrows(BadRequestException.class,
			() -> accountService.createDepositAccount(request));
		assertEquals(ErrorCode.WITHDRAWAL_ACCOUNT_IS_NOT_ACTIVE.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("예금 가입 시 출금 계좌의 잔액이 부족하면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenWithdrawalAccountNotHasEnoughBalance() {
		Long productId = 2L;
		Long memberId = 100L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");
		when(request.initDepositAmount()).thenReturn(50_000_000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002")).thenReturn(
			Optional.of(withdrawalAccount));
		lenient().when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(mock(Account.class)));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_HAS_NOT_ENOUGH_BALANCE)).when(withdrawalAccount)
			.verifyWithdrawalAccount(memberId, 50_000_000L);

		BadRequestException ex = assertThrows(BadRequestException.class,
			() -> accountService.createDepositAccount(request));
		assertEquals(ErrorCode.WITHDRAWAL_ACCOUNT_HAS_NOT_ENOUGH_BALANCE.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("예금 가입 시 존재하지 않는 환급 계좌를 입력하면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenPayoutAccountNotExist() {
		Long productId = 2L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		when(accountRepository.findAccountByFullAccountNumber("100001000002")).thenReturn(
			Optional.of(mock(Account.class)));
		when(accountRepository.findAccountByFullAccountNumber("100001000003")).thenReturn(Optional.empty());

		BadRequestException ex = assertThrows(BadRequestException.class,
			() -> accountService.createDepositAccount(request));
		assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("예금 가입 시 본인 소유가 아닌 환급 계좌를 입력하면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenPayoutAccountNotOwned() {
		Long productId = 2L;
		Long memberId = 100L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002")).thenReturn(
			Optional.of(withdrawalAccount));

		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000003")).thenReturn(Optional.of(payoutAccount));

		doThrow(new BadRequestException(ErrorCode.MATURITY_PAYOUT_ACCOUNT_NOT_OWNED)).when(payoutAccount)
			.verifyPayoutAccount(memberId);

		BadRequestException ex = assertThrows(BadRequestException.class,
			() -> accountService.createDepositAccount(request));
		assertEquals(ErrorCode.MATURITY_PAYOUT_ACCOUNT_NOT_OWNED.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("예금 가입 시 환급 계좌가 활성화 상태가 아니면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenPayoutAccountNotActive() {
		Long productId = 2L;
		Long memberId = 100L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");
		when(request.initDepositAmount()).thenReturn(5000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002")).thenReturn(
			Optional.of(withdrawalAccount));

		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000003")).thenReturn(Optional.of(payoutAccount));

		doNothing().when(withdrawalAccount).verifyWithdrawalAccount(memberId, 5000L);
		doThrow(new BadRequestException(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ACTIVE)).when(payoutAccount)
			.verifyPayoutAccount(memberId);

		BadRequestException ex = assertThrows(BadRequestException.class,
			() -> accountService.createDepositAccount(request));
		assertEquals(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ACTIVE.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("사용자는 적금 상품에 가입할 수 있다.")
	void createInstallmentAccount_success() {
		Long productId = 3L;
		Long memberId = 100L;
		String withdrawalAccountNumber = "100001000004";
		String payoutAccountNumber = "100001000005";
		Long monthlyInstallmentAmount = 1000L;

		InstallmentAccountRegisterRequest request = mock(InstallmentAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);
		when(request.monthlyInstallmentAmount()).thenReturn(monthlyInstallmentAmount);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.getInterestRate()).thenReturn(5.0);
		when(accountProduct.getSubscriptionPeriod()).thenReturn(24);

		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber)).thenReturn(
			Optional.of(withdrawalAccount));
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber)).thenReturn(
			Optional.of(payoutAccount));

		doNothing().when(withdrawalAccount).verifyWithdrawalAccount(memberId, monthlyInstallmentAmount);
		doNothing().when(payoutAccount).verifyPayoutAccount(memberId);

		String newAccountNumber = "300001000010";
		when(accountRepository.getNextAccountNumber("300", "001")).thenReturn(newAccountNumber);

		when(withdrawalAccount.getAccountId()).thenReturn(111L);
		when(payoutAccount.getAccountId()).thenReturn(222L);

		Double interestRate = accountProduct.getInterestRate();
		LocalDate expectedExpiryDate = LocalDate.now().plusMonths(24L);

		Account installmentAccount = mock(Account.class);
		when(request.toEntity(
			eq(newAccountNumber),
			eq(interestRate),
			eq(111L),
			eq(222L),
			eq(expectedExpiryDate)))
			.thenReturn(installmentAccount);

		when(accountRepository
			.saveInstallmentAccount(
				installmentAccount,
				accountProduct.getSubscriptionPeriod())
		).thenReturn(installmentAccount);

		AccountRegisterResponse response = accountService.createInstallmentAccount(request);
		assertNotNull(response);
		verify(accountProductRepository).existsAccountProductById(productId);
		verify(accountProductRepository).getAccountProductById(productId);
		verify(accountRepository).findAccountByFullAccountNumber(withdrawalAccountNumber);
		verify(accountRepository).findAccountByFullAccountNumber(payoutAccountNumber);
		verify(withdrawalAccount).verifyWithdrawalAccount(memberId, monthlyInstallmentAmount);
		verify(payoutAccount).verifyPayoutAccount(memberId);
		verify(accountRepository).getNextAccountNumber("300", "001");
		verify(accountRepository).saveInstallmentAccount(installmentAccount, 24);
	}

	@Test
	@DisplayName("적금 가입 시 존재하지 않는 출금 계좌를 입력하면 에러가 발생한다.")
	void creatInstallmentAccount_shouldThrowExceptionWhenWithdrawalAccountNotExist() {
		Long productId = 3L;
		InstallmentAccountRegisterRequest request = mock(InstallmentAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		String withdrawalAccountNumber = "100001000004";
		String payoutAccountNumber = "100001000005";
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber)).thenReturn(Optional.empty());
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber)).thenReturn(
			Optional.of(mock(Account.class)));

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> accountService.createInstallmentAccount(request));
		assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getCode(), exception.getCode());
		verify(accountRepository).findAccountByFullAccountNumber(withdrawalAccountNumber);
	}

	@Test
	@DisplayName("적금 가입 시 본인 소유가 아닌 출금 계좌를 입력하면 에러가 발생한다.")
	void createInstallmentAccount_shouldThrowExceptionWhenWithdrawalAccountNotOwned() {
		Long productId = 3L;
		Long memberId = 100L;
		InstallmentAccountRegisterRequest request = mock(InstallmentAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		String withdrawalAccountNumber = "100001000004";
		String payoutAccountNumber = "100001000005";
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);
		when(request.monthlyInstallmentAmount()).thenReturn(1000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber)).thenReturn(
			Optional.of(withdrawalAccount));
		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber)).thenReturn(
			Optional.of(payoutAccount));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_NOT_OWNED)).when(withdrawalAccount)
			.verifyWithdrawalAccount(memberId, 1000L);

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> accountService.createInstallmentAccount(request));
		assertEquals(ErrorCode.WITHDRAWAL_ACCOUNT_NOT_OWNED.getCode(), exception.getCode());
	}

	@Test
	@DisplayName("적금 가입 시 출금 계좌가 활성화 상태가 아니면 에러가 발생한다.")
	void createInstallmentAccount_shouldThrowExceptionWhenWithdrawalAccountNotActive() {
		Long productId = 3L;
		Long memberId = 100L;
		InstallmentAccountRegisterRequest request = mock(InstallmentAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		String withdrawalAccountNumber = "100001000004";
		String payoutAccountNumber = "100001000005";
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);
		when(request.monthlyInstallmentAmount()).thenReturn(1000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber)).thenReturn(
			Optional.of(withdrawalAccount));
		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber)).thenReturn(
			Optional.of(payoutAccount));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_IS_NOT_ACTIVE)).when(withdrawalAccount)
			.verifyWithdrawalAccount(memberId, 1000L);

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> accountService.createInstallmentAccount(request));
		assertEquals(ErrorCode.WITHDRAWAL_ACCOUNT_IS_NOT_ACTIVE.getCode(), exception.getCode());
	}

	@Test
	@DisplayName("적금 가입 시 존재하지 않는 환급 계좌를 입력하면 에러가 발생한다.")
	void createInstallmentAccount_shouldThrowExceptionWhenPayoutAccountNotExist() {
		Long productId = 3L;
		InstallmentAccountRegisterRequest request = mock(InstallmentAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		String withdrawalAccountNumber = "100001000004";
		String payoutAccountNumber = "100001000005";
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber)).thenReturn(
			Optional.of(mock(Account.class)));
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber)).thenReturn(Optional.empty());

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> accountService.createInstallmentAccount(request));
		assertEquals(ErrorCode.ACCOUNT_NOT_FOUND.getCode(), exception.getCode());
	}

	@Test
	@DisplayName("적금 가입 시 본인 소유가 아닌 환급 계좌를 입력하면 에러가 발생한다.")
	void createInstallmentAccount_shouldThrowExceptionWhenPayoutAccountNotOwned() {
		Long productId = 3L;
		Long memberId = 100L;
		InstallmentAccountRegisterRequest request = mock(InstallmentAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		String withdrawalAccountNumber = "100001000004";
		String payoutAccountNumber = "100001000005";
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);
		when(request.monthlyInstallmentAmount()).thenReturn(1000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber)).thenReturn(
			Optional.of(withdrawalAccount));
		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber)).thenReturn(
			Optional.of(payoutAccount));

		doThrow(new BadRequestException(ErrorCode.MATURITY_PAYOUT_ACCOUNT_NOT_OWNED)).when(payoutAccount)
			.verifyPayoutAccount(memberId);

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> accountService.createInstallmentAccount(request));
		assertEquals(ErrorCode.MATURITY_PAYOUT_ACCOUNT_NOT_OWNED.getCode(), exception.getCode());
	}

	@Test
	@DisplayName("적금 가입 시 환급 계좌가 활성화 상태가 아니면 에러가 발생한다.")
	void createInstallmentAccount_shouldThrowExceptionWhenPayoutAccountNotActive() {
		Long productId = 3L;
		Long memberId = 100L;
		InstallmentAccountRegisterRequest request = mock(InstallmentAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		String withdrawalAccountNumber = "100001000004";
		String payoutAccountNumber = "100001000005";
		when(request.withdrawalAccountNumber()).thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber()).thenReturn(payoutAccountNumber);
		when(request.monthlyInstallmentAmount()).thenReturn(1000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProductRepository.existsAccountProductById(productId)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber)).thenReturn(
			Optional.of(withdrawalAccount));
		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber)).thenReturn(
			Optional.of(payoutAccount));

		doNothing().when(withdrawalAccount).verifyWithdrawalAccount(memberId, 1000L);
		doThrow(new BadRequestException(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ACTIVE)).when(payoutAccount)
			.verifyPayoutAccount(memberId);

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> accountService.createInstallmentAccount(request));
		assertEquals(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ACTIVE.getCode(), exception.getCode());
	}

	@Test
	@DisplayName("사용자는 잔액을 조회할 수 있다.")
	void getAccountBalance_success() {
		String accountNumber = "100001000002";
		String password = "correctPassword";
		Long balance = 1000L;
		String bankName = "삼성은행";

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.password(password)
			.build();

		BalanceResponse expectedResponse = new BalanceResponse(accountNumber, bankName, balance);

		given(accountRepository.findAccountByFullAccountNumber(accountNumber))
			.willReturn(Optional.of(account));
		given(accountRepository.getAccountBalance(accountNumber))
			.willReturn(expectedResponse);

		BalanceRequest request = new BalanceRequest(accountNumber, password);
		BalanceResponse actualResponse = accountService.getAccountBalance(request);

		assertNotNull(actualResponse);
		assertEquals(expectedResponse.accountNumber(), actualResponse.accountNumber());
		assertEquals(expectedResponse.bankName(), actualResponse.bankName());
		assertEquals(expectedResponse.balance(), actualResponse.balance());

		verify(accountRepository).findAccountByFullAccountNumber(accountNumber);
		verify(accountRepository).getAccountBalance(accountNumber);
	}

	@Test
	@DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다.")
	void should_throw_exception_when_password_is_incorrect() {
		String accountNumber = "100001000002";
		String correctPassword = "correctPassword";
		String wrongPassword = "wrongPassword";

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.password(correctPassword)
			.build();

		given(accountRepository.findAccountByFullAccountNumber(accountNumber))
			.willReturn(Optional.of(account));

		BalanceRequest request = new BalanceRequest(accountNumber, wrongPassword);

		Assertions.assertThatThrownBy(() -> accountService.getAccountBalance(request))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("비밀번호가 일치하지 않습니다");

		verify(accountRepository).findAccountByFullAccountNumber(accountNumber);
		verify(accountRepository, never()).getAccountBalance(any());
	}

	@Test
	@DisplayName("할부 납입일이 도래한 계좌 정상 처리 - 성공 결과만 반환")
	void handleInstallmentAccountSchedule_success() {
		// given
		LocalDate today = LocalDate.now();
		InstallmentSchedule schedule1 = createInstallmentSchedule(1L, 2L, 10000L, 1, 15);
		InstallmentSchedule schedule2 = createInstallmentSchedule(3L, 4L, 20000L, 2, 15);

		given(accountRepository.findInstallmentScheduleByInstallmentDateAndScheduled(today))
			.willReturn(List.of(schedule1, schedule2));

		// 계좌 조회 성공
		given(accountRepository.findAccountById(1L))
			.willReturn(Optional.of(createAccount(1L, 10000L)));
		given(accountRepository.findAccountById(2L))
			.willReturn(Optional.of(createAccount(2L, 0L)));
		given(accountRepository.findAccountById(3L))
			.willReturn(Optional.of(createAccount(3L, 20000L)));
		given(accountRepository.findAccountById(4L))
			.willReturn(Optional.of(createAccount(4L, 0L)));

		// when
		InstallmentPaymentProcessingResult result = accountService.handleInstallmentAccountSchedule();

		// then
		assertThat(result.total()).isEqualTo(2);
		assertThat(result.success()).isEqualTo(2);
		assertThat(result.failed()).isEmpty();

		// verify
		then(transferFacade).should(times(2)).transfer(any(TransferInfo.class));
		then(accountRepository).should(times(2)).saveInstallmentSchedule(any(InstallmentSchedule.class));
	}

	@Test
	@DisplayName("출금 계좌 조회 실패 - ACCOUNT_NOT_FOUND 에러 발생")
	void handleInstallmentAccountSchedule_withdrawalAccountNotFound() {
		// given
		LocalDate today = LocalDate.now();
		InstallmentSchedule schedule = createInstallmentSchedule(1L, 2L, 10000L, 1, 15);

		given(accountRepository.findInstallmentScheduleByInstallmentDateAndScheduled(today))
			.willReturn(List.of(schedule));

		given(accountRepository.findAccountById(1L))
			.willReturn(Optional.empty());

		// when
		InstallmentPaymentProcessingResult result = accountService.handleInstallmentAccountSchedule();

		// then
		assertThat(result.total()).isEqualTo(1);
		assertThat(result.success()).isEqualTo(0);
		assertThat(result.failed()).hasSize(1);
		assertThat(result.failed().get(0).accountId()).isEqualTo(2L);
		assertThat(result.failed().get(0).reason()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());

		// verify
		then(transferFacade).should(never()).transfer(any());
		then(accountRepository).should(times(1)).saveInstallmentSchedule(any(InstallmentSchedule.class));
	}

	@Test
	@DisplayName("적금 계좌 조회 실패 - ACCOUNT_NOT_FOUND 에러 발생")
	void handleInstallmentAccountSchedule_installmentAccountNotFound() {
		// given
		LocalDate today = LocalDate.now();
		InstallmentSchedule schedule = createInstallmentSchedule(1L, 2L, 10000L, 1, 15);

		given(accountRepository.findInstallmentScheduleByInstallmentDateAndScheduled(today))
			.willReturn(List.of(schedule));

		given(accountRepository.findAccountById(1L))
			.willReturn(Optional.of(createAccount(1L, 10000L)));
		given(accountRepository.findAccountById(2L))
			.willReturn(Optional.empty());

		// when
		InstallmentPaymentProcessingResult result = accountService.handleInstallmentAccountSchedule();

		// then
		assertThat(result.total()).isEqualTo(1);
		assertThat(result.success()).isEqualTo(0);
		assertThat(result.failed()).hasSize(1);
		assertThat(result.failed().get(0).accountId()).isEqualTo(2L);
		assertThat(result.failed().get(0).reason()).isEqualTo(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());

		// verify
		then(transferFacade).should(never()).transfer(any());
		then(accountRepository).should(times(1)).saveInstallmentSchedule(any(InstallmentSchedule.class));
	}

	@Test
	@DisplayName("잔액 부족으로 이체 실패 - NOT_ENOUGH_BALANCE 에러 발생")
	void handleInstallmentAccountSchedule_insufficientBalance() {
		// given
		LocalDate today = LocalDate.now();
		InstallmentSchedule schedule = createInstallmentSchedule(1L, 2L, 10000L, 1, 15);

		given(accountRepository.findInstallmentScheduleByInstallmentDateAndScheduled(today))
			.willReturn(List.of(schedule));

		given(accountRepository.findAccountById(1L))
			.willReturn(Optional.of(createAccount(1L, 5000L))); // 잔액 부족
		given(accountRepository.findAccountById(2L))
			.willReturn(Optional.of(createAccount(2L, 0L)));

		doThrow(new BadRequestException(ErrorCode.NOT_ENOUGH_BALANCE))
			.when(transferFacade).transfer(any(TransferInfo.class));

		// when
		InstallmentPaymentProcessingResult result = accountService.handleInstallmentAccountSchedule();

		// then
		assertThat(result.total()).isEqualTo(1);
		assertThat(result.success()).isEqualTo(0);
		assertThat(result.failed()).hasSize(1);
		assertThat(result.failed().get(0).accountId()).isEqualTo(2L);
		assertThat(result.failed().get(0).reason()).isEqualTo(ErrorCode.NOT_ENOUGH_BALANCE.getMessage());

		// verify
		then(transferFacade).should(times(1)).transfer(any(TransferInfo.class));
		then(accountRepository).should(times(1)).saveInstallmentSchedule(any(InstallmentSchedule.class));
	}

	@Test
	@DisplayName("동일 계좌 이체 시도 - SAME_ACCOUNT_TRANSFER 에러 발생")
	void handleInstallmentAccountSchedule_sameAccountTransfer() {
		// given
		LocalDate today = LocalDate.now();
		// 출금계좌와 적금계좌가 동일한 경우
		InstallmentSchedule schedule = createInstallmentSchedule(1L, 1L, 10000L, 1, 15);

		given(accountRepository.findInstallmentScheduleByInstallmentDateAndScheduled(today))
			.willReturn(List.of(schedule));

		given(accountRepository.findAccountById(1L))
			.willReturn(Optional.of(createAccount(1L, 10000L)));

		doThrow(new BadRequestException(ErrorCode.SAME_ACCOUNT_TRANSFER))
			.when(transferFacade).transfer(any(TransferInfo.class));

		// when
		InstallmentPaymentProcessingResult result = accountService.handleInstallmentAccountSchedule();

		// then
		assertThat(result.total()).isEqualTo(1);
		assertThat(result.success()).isEqualTo(0);
		assertThat(result.failed()).hasSize(1);
		assertThat(result.failed().get(0).accountId()).isEqualTo(1L);
		assertThat(result.failed().get(0).reason()).isEqualTo(ErrorCode.SAME_ACCOUNT_TRANSFER.getMessage());

		// verify
		then(transferFacade).should(times(1)).transfer(any(TransferInfo.class));
		then(accountRepository).should(times(1)).saveInstallmentSchedule(any(InstallmentSchedule.class));
	}

	@Test
	@DisplayName("회원 정보 없음 - MEMBER_NOT_FOUND 에러 발생")
	void handleInstallmentAccountSchedule_memberNotFound() {
		// given
		LocalDate today = LocalDate.now();
		InstallmentSchedule schedule = createInstallmentSchedule(1L, 2L, 10000L, 1, 15);

		given(accountRepository.findInstallmentScheduleByInstallmentDateAndScheduled(today))
			.willReturn(List.of(schedule));

		given(accountRepository.findAccountById(1L))
			.willReturn(Optional.of(createAccount(1L, 10000L)));
		given(accountRepository.findAccountById(2L))
			.willReturn(Optional.of(createAccount(2L, 0L)));

		doThrow(new BadRequestException(ErrorCode.MEMBER_NOT_FOUND))
			.when(transferFacade).transfer(any(TransferInfo.class));

		// when
		InstallmentPaymentProcessingResult result = accountService.handleInstallmentAccountSchedule();

		// then
		assertThat(result.total()).isEqualTo(1);
		assertThat(result.success()).isEqualTo(0);
		assertThat(result.failed()).hasSize(1);
		assertThat(result.failed().get(0).accountId()).isEqualTo(2L);
		assertThat(result.failed().get(0).reason()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getMessage());

		// verify
		then(transferFacade).should(times(1)).transfer(any(TransferInfo.class));
		then(accountRepository).should(times(1)).saveInstallmentSchedule(any(InstallmentSchedule.class));
	}

	@Test
	@DisplayName("예상치 못한 런타임 예외 발생 시에도 실패 목록에 추가")
	void handleInstallmentAccountSchedule_unexpectedException() {
		// given
		LocalDate today = LocalDate.now();
		InstallmentSchedule schedule = createInstallmentSchedule(1L, 2L, 10000L, 1, 15);

		given(accountRepository.findInstallmentScheduleByInstallmentDateAndScheduled(today))
			.willReturn(List.of(schedule));

		given(accountRepository.findAccountById(1L))
			.willReturn(Optional.of(createAccount(1L, 10000L)));
		given(accountRepository.findAccountById(2L))
			.willReturn(Optional.of(createAccount(2L, 0L)));

		// transfer 실패 시뮬레이션
		doThrow(new RuntimeException("데이터베이스 연결 실패"))
			.when(transferFacade).transfer(any(TransferInfo.class));

		// 저장 동작 모킹 (void 메서드 처리)
		doNothing().when(accountRepository).saveInstallmentSchedule(any(InstallmentSchedule.class));

		// when
		InstallmentPaymentProcessingResult result = accountService.handleInstallmentAccountSchedule();

		// then
		assertThat(result.total()).isEqualTo(1);
		assertThat(result.success()).isEqualTo(0);
		assertThat(result.failed()).hasSize(1);
		assertThat(result.failed().get(0).accountId()).isEqualTo(2L);
		assertThat(result.failed().get(0).reason()).contains("데이터베이스 연결 실패");

		// verify
		verify(accountRepository, times(1)).saveInstallmentSchedule(any(InstallmentSchedule.class));
	}

	private InstallmentSchedule createInstallmentSchedule(
		Long withdrawalAccountId,
		Long installmentAccountId,
		Long amount,
		int sequence,
		int initialDay
	) {
		return InstallmentSchedule.builder()
			.withdrawalAccountId(withdrawalAccountId)
			.installmentAccountId(installmentAccountId)
			.installmentAmount(amount)
			.installmentSequence(sequence)
			.initialInstallmentScheduleDay(initialDay)
			.installmentScheduledDate(LocalDate.now().withDayOfMonth(initialDay))
			.build();
	}

	private Account createAccount(Long accountId, Long balance) {
		return Account.builder()
			.accountId(accountId)
			.accountBalance(balance)
			.accountStatus(AccountStatus.ACTIVE)
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber("12345678")
			.password("1234")
			.dailyTransferLimit(1000000L)
			.singleTransferLimit(1000000L)
			.interestRate(0.0)
			.build();
	}
}
