package bank.donghang.core.account.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.request.BalanceRequest;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
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
	@DisplayName("사용자는 자유입출금 계좌를 생성할 수 있다.")
	void createDemandAccount() {
		Long productId = 1L;
		DemandAccountRegisterRequest request = mock(DemandAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(1L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		doReturn(0.05).when(accountProduct).getInterestRate();
		when(accountProductRepository.getAccountProductById(productId))
			.thenReturn(accountProduct);
		when(accountProductRepository.existsAccountProductById(productId))
			.thenReturn(true);

		String nextAccountNumber = "000001";
		when(accountRepository.getNextAccountNumber("100", "001"))
			.thenReturn(nextAccountNumber);

		Account account = mock(Account.class);
		when(request.toEntity(nextAccountNumber, accountProduct.getInterestRate()))
			.thenReturn(account);

		when(accountRepository.saveAccount(account)).thenReturn(account);

		AccountRegisterResponse response = accountService.createDemandAccount(request);

		assertNotNull(response);
		verify(accountProductRepository).getAccountProductById(productId);
		verify(accountRepository).getNextAccountNumber("100", "001");
		verify(accountRepository).saveAccount(account);
	}

	@Test
	@DisplayName("존재하지 않는 계좌 상품으로 계좌 생성을 시도하면 에러가 발생한다. (Demand 계좌)")
	void createDemandAccount_shouldThrowExceptionWhenAccountProductNotExist() {
		Long productId = 2000L;
		DemandAccountRegisterRequest request
			= new DemandAccountRegisterRequest(1L, productId, "1234", true);

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
		when(request.accountProductId())
			.thenReturn(productId);
		when(request.memberId())
			.thenReturn(memberId);
		when(request.withdrawalAccountNumber())
			.thenReturn(withdrawalAccountNumber);
		when(request.payoutAccountNumber())
			.thenReturn(payoutAccountNumber);
		when(request.initDepositAmount())
			.thenReturn(5000L);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct())
			.thenReturn(true);
		when(accountProduct.getInterestRate())
			.thenReturn(0.5);
		when(accountProduct.getSubscriptionPeriod())
			.thenReturn(12L);

		when(accountProductRepository.existsAccountProductById(productId))
			.thenReturn(true);
		when(accountProductRepository.getAccountProductById(productId))
			.thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		Account payoutAccount = mock(Account.class);

		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber))
			.thenReturn(Optional.of(withdrawalAccount));
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber))
			.thenReturn(Optional.of(payoutAccount));

		doNothing().when(withdrawalAccount).verifyWithdrawalAccount(memberId, 5000L);
		doNothing().when(payoutAccount).verifyPayoutAccount(memberId);

		String newDepositAccountNumber = "200001000010";
		when(accountRepository.getNextAccountNumber("200", "001"))
			.thenReturn(newDepositAccountNumber);

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

		when(accountRepository.saveAccount(depositAccount))
			.thenReturn(depositAccount);

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
		when(accountProduct.getSubscriptionPeriod()).thenReturn(24L);

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
		when(request.toEntity(eq(newAccountNumber), eq(interestRate), eq(111L), eq(222L),
			eq(expectedExpiryDate))).thenReturn(installmentAccount);

		when(accountRepository.saveInstallmentAccount(installmentAccount)).thenReturn(installmentAccount);

		AccountRegisterResponse response = accountService.createInstallmentAccount(request);

		assertNotNull(response);
		verify(accountProductRepository).existsAccountProductById(productId);
		verify(accountProductRepository).getAccountProductById(productId);
		verify(accountRepository).findAccountByFullAccountNumber(withdrawalAccountNumber);
		verify(accountRepository).findAccountByFullAccountNumber(payoutAccountNumber);
		verify(withdrawalAccount).verifyWithdrawalAccount(memberId, monthlyInstallmentAmount);
		verify(payoutAccount).verifyPayoutAccount(memberId);
		verify(accountRepository).getNextAccountNumber("300", "001");
		verify(accountRepository).saveInstallmentAccount(installmentAccount);
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

	@DisplayName("사용자는 잔액을 조회할 수 있다.")
	void can_get_account_balance() {
		Long balance = 1000L;
		String accountNumber = "100001000002";
		String password = "correctPassword";
		String bankName = "삼성은행";

		Account account = Account.builder()
			.accountNumber(accountNumber)
			.password(password)
			.build();

		BalanceResponse expectedResponse = new BalanceResponse(
			accountNumber,
			bankName,
			balance
		);

		given(accountRepository.findAccountByFullAccountNumber(accountNumber))
			.willReturn(Optional.of(account));

		given(accountRepository.getAccountBalance(accountNumber))
			.willReturn(expectedResponse);

		BalanceRequest request = new BalanceRequest(accountNumber, password);

		BalanceResponse actualResponse = accountService.getAccountBalance(request);

		Assertions.assertThat(actualResponse).isNotNull();
		Assertions.assertThat(actualResponse.accountNumber()).isEqualTo(accountNumber);
		Assertions.assertThat(actualResponse.bankName()).isEqualTo(bankName);
		Assertions.assertThat(actualResponse.balance()).isEqualTo(balance);

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
}
