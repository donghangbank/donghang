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
import bank.donghang.core.account.dto.request.AccountRegisterRequest;
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
	void createAccount() {
		// Arrange
		Long productId = 1L;
		AccountRegisterRequest request = mock(AccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);

		AccountProduct accountProduct = mock(AccountProduct.class);
		when(accountProduct.getInterestRate()).thenReturn(0.05);
		when(accountProductRepository.getAccountProductById(productId))
			.thenReturn(Optional.of(accountProduct));

		String nextAccountNumber = "1234567890";
		when(accountRepository.getNextAccountNumber("100", "001"))
			.thenReturn(nextAccountNumber);

		Account account = mock(Account.class);
		when(request.toEntity(nextAccountNumber, accountProduct.getInterestRate()))
			.thenReturn(account);

		when(accountRepository.saveAccount(account))
			.thenReturn(account);

		AccountRegisterResponse response = accountService.createAccount(request);

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
		AccountRegisterRequest request = mock(AccountRegisterRequest.class);
		when(request.accountProductId()).thenReturn(productId);

		when(accountProductRepository.getAccountProductById(productId))
			.thenReturn(Optional.empty());

		BadRequestException exception = assertThrows(
			BadRequestException.class,
			() -> accountService.createAccount(request)
		);
		assertEquals(ErrorCode.PRODUCT_NOT_FOUND.getCode(), exception.getCode());
		verify(accountProductRepository).getAccountProductById(productId);
	}
}
