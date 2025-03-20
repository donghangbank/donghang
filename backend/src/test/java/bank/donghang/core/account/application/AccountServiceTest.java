package bank.donghang.core.account.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@InjectMocks
	private AccountService accountService;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AccountProductRepository accountProductRepository;

	@Test
	@DisplayName("사용자는 자유입출금 계좌를 생성할 수 있다.")
	void createDemandAccount() {
		// Arrange
		Long productId = 1L;
		DemandAccountRegisterRequest request = mock(DemandAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.getInterestRate()).thenReturn(0.05);
		when(accountProductRepository.getAccountProductById(productId))
			.thenReturn(Optional.of(accountProduct));

		String nextAccountNumber = "000001";
		when(accountRepository.getNextAccountNumber("100", "001"))
			.thenReturn(nextAccountNumber);

		Account account = mock(Account.class);
		when(request.toEntity(nextAccountNumber, accountProduct.getInterestRate()))
			.thenReturn(account);

		when(accountRepository.saveAccount(account))
			.thenReturn(account);

		AccountRegisterResponse response = accountService.createDemandAccount(request);

		assertNotNull(response);
		verify(accountProductRepository).getAccountProductById(productId);
		verify(accountRepository).getNextAccountNumber("100", "001");
		verify(request).toEntity(nextAccountNumber, accountProduct.getInterestRate());
		verify(accountRepository).saveAccount(account);
	}

	@Test
	@DisplayName("존재하지 않는 계좌 상품으로 계좌 생성을 시도하면 에러가 발생한다.")
	void createAccount_shouldThrowExceptionWhenAccountProductNotExist() {
		Long productId = 2000L;
		DemandAccountRegisterRequest request = mock(DemandAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);

		when(accountProductRepository.getAccountProductById(productId))
			.thenReturn(Optional.empty());

		BadRequestException exception = assertThrows(
			BadRequestException.class,
			() -> accountService.createDemandAccount(request)
		);
		assertEquals(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND.getCode(), exception.getCode());
		verify(accountProductRepository).getAccountProductById(productId);
	}

	@Test
	@DisplayName("사용자는 예금 상품에 가입할 수 있다.")
	void createDepositAccount() {
		// Arrange
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

		when(accountProductRepository.getAccountProductByIdIfExist(productId))
			.thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		Account payoutAccount = mock(Account.class);

		when(accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber))
			.thenReturn(Optional.of(withdrawalAccount));
		when(accountRepository.findAccountByFullAccountNumber(payoutAccountNumber))
			.thenReturn(Optional.of(payoutAccount));

		doNothing().when(withdrawalAccount).verifyWithdrawalAccount(memberId, 5000L);
		doNothing().when(payoutAccount).verifyPayoutAccount(memberId);

		String newDepositAccountNumber = "200001000010"; // 새 예금 계좌번호
		when(accountRepository.getNextAccountNumber("200", "001"))
			.thenReturn(newDepositAccountNumber);

		when(withdrawalAccount.getAccountId()).thenReturn(101L);
		when(payoutAccount.getAccountId()).thenReturn(202L);

		// 'request.toEntity(...)'도 인자 매처 통일
		Account depositAccount = mock(Account.class);
		when(request.toEntity(
			eq(newDepositAccountNumber),                 // String
			eq(0.5),                              // double
			eq(101L),                              // withdrawalAccountId
			eq(202L),                              // payoutAccountId
			eq(0L)                                 // 가입 시 현재 계좌 잔액 = 0
		)).thenReturn(depositAccount);

		when(accountRepository.saveAccount(depositAccount)).thenReturn(depositAccount);

		AccountRegisterResponse response = accountService.createDepositAccount(request);

		assertNotNull(response);
	}

	@Test
	@DisplayName("예금 가입 시 예금 상품이 존재하지 않으면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenAccountProductNotExist() {
		// Arrange
		Long productId = 9999L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);

		// getAccountProductByIdIfExist 호출 시, 예외를 발생시키도록 구성한다고 가정
		doThrow(new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND))
			.when(accountProductRepository).getAccountProductByIdIfExist(productId);

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);
		assertEquals(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND.getCode(), ex.getCode());
		verify(accountProductRepository).getAccountProductByIdIfExist(productId);
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
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.getAccountProductByIdIfExist(productId))
			.thenReturn(accountProduct);

		when(accountRepository.findAccountByFullAccountNumber("100001000002"))
			.thenReturn(Optional.empty());
		when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(mock(Account.class)));

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);

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
		when(accountProductRepository.getAccountProductByIdIfExist(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002"))
			.thenReturn(Optional.of(withdrawalAccount));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_NOT_OWNED))
			.when(withdrawalAccount).verifyWithdrawalAccount(memberId, 5000L);

		when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(mock(Account.class)));

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);

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
		when(accountProductRepository.getAccountProductByIdIfExist(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002"))
			.thenReturn(Optional.of(withdrawalAccount));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_IS_NOT_ACTIVE))
			.when(withdrawalAccount).verifyWithdrawalAccount(memberId, 5000L);

		when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(mock(Account.class)));

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);

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
		when(accountProductRepository.getAccountProductByIdIfExist(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002"))
			.thenReturn(Optional.of(withdrawalAccount));

		doThrow(new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_HAS_NOT_ENOUGH_BALANCE))
			.when(withdrawalAccount).verifyWithdrawalAccount(memberId, 50_000_000L);

		when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(mock(Account.class)));

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);

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
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.getAccountProductByIdIfExist(productId)).thenReturn(accountProduct);

		when(accountRepository.findAccountByFullAccountNumber("100001000002"))
			.thenReturn(Optional.of(mock(Account.class)));
		when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.empty());

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);

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
		when(accountProductRepository.getAccountProductByIdIfExist(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002"))
			.thenReturn(Optional.of(withdrawalAccount));

		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(payoutAccount));

		doThrow(new BadRequestException(ErrorCode.MATURITY_PAYOUT_ACCOUNT_NOT_OWNED))
			.when(payoutAccount).verifyPayoutAccount(memberId);

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);

		assertEquals(ErrorCode.MATURITY_PAYOUT_ACCOUNT_NOT_OWNED.getCode(), ex.getCode());
	}

	@Test
	@DisplayName("예금 가입 시 환급 계좌가 활성화 상태가 아니면 에러가 발생한다.")
	void createDepositAccount_shouldThrowExceptionWhenPayoutAccountNotActive() {
		// Arrange
		Long productId = 2L;
		Long memberId = 100L;
		DepositAccountRegisterRequest request = mock(DepositAccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);
		when(request.memberId()).thenReturn(memberId);
		when(request.withdrawalAccountNumber()).thenReturn("100001000002");
		when(request.payoutAccountNumber()).thenReturn("100001000003");

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.isDepositProduct()).thenReturn(true);
		when(accountProductRepository.getAccountProductByIdIfExist(productId)).thenReturn(accountProduct);

		Account withdrawalAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000002"))
			.thenReturn(Optional.of(withdrawalAccount));

		Account payoutAccount = mock(Account.class);
		when(accountRepository.findAccountByFullAccountNumber("100001000003"))
			.thenReturn(Optional.of(payoutAccount));

		doNothing().when(withdrawalAccount).verifyWithdrawalAccount(memberId, 0L);
		doThrow(new BadRequestException(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ACTIVE))
			.when(payoutAccount).verifyPayoutAccount(memberId);

		BadRequestException ex = assertThrows(
			BadRequestException.class,
			() -> accountService.createDepositAccount(request)
		);

		assertEquals(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ACTIVE.getCode(), ex.getCode());
	}
}
