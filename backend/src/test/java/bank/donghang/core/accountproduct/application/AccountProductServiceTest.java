package bank.donghang.core.accountproduct.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.bank.domain.Bank;
import bank.donghang.core.bank.domain.repository.BankRepository;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AccountProductServiceTest {

	@InjectMocks
	private AccountProductService accountProductService;

	@Mock
	private AccountProductRepository accountProductRepository;

	@Mock
	private BankRepository bankRepository;

	@Test
	@DisplayName("계좌 상품 목록을 조회할 수 있다.")
	void getAllAccountProducts_shouldReturnProductList() {
		// given
		List<AccountProductSummary> mockSummaries = List.of(
				new AccountProductSummary(
						1L,
						"Saving Account",
						1L,
						"Mock Bank",
						"https://logo.mockbank.com/logo.png",
						0.3,
						null,
						0L,
						0L,
						AccountProductType.DEMAND.name()),
				new AccountProductSummary(
						2L,
						"Checking Account",
						1L,
						"Mock Bank",
						"https://logo.mockbank.com/logo.png",
						0.2,
						null,
						0L,
						0L,
						AccountProductType.DEMAND.name())
		);

		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, mockSummaries, false);

		when(accountProductRepository.getPaginatedAccountProductsByAccountProductType(
				null,
				null)
		)
				.thenReturn(expectedPage);

		// when
		PageInfo<AccountProductSummary> response = accountProductService.getAllAccountProducts(null);

		// then
		assertThat(response.data()).hasSize(2);
		assertThat(response.data().get(0).accountProductName()).isEqualTo("Saving Account");
		assertThat(response.data().get(1).accountProductName()).isEqualTo("Checking Account");
	}

	@Test
	@DisplayName("특정 계좌 상품의 상세 정보를 가져올 수 있다.")
	void getAccountProductDetail_shouldReturnDetail() {
		AccountProduct mockProduct = AccountProduct.builder()
				.accountProductId(1L)
				.accountProductName("Saving Account")
				.accountProductDescription("High Interest Savings")
				.bankId(1L)
				.interestRate(0.3)
				.accountProductType(AccountProductType.DEMAND)
				.subscriptionPeriod(null)
				.rateDescription("기본 이율")
				.minSubscriptionBalance(0L)
				.maxSubscriptionBalance(0L)
				.build();

		when(accountProductRepository.existsAccountProductById(1L)).thenReturn(true);
		when(accountProductRepository.getAccountProductById(1L)).thenReturn(mockProduct);

		AccountProductDetail result = accountProductService.getAccountProductDetail(1L);

		assertThat(result.productName()).isEqualTo("Saving Account");
		assertThat(result.productDescription()).isEqualTo("High Interest Savings");
	}

	@Test
	@DisplayName("존재하지 않는 계좌 상품을 조회하면 예외가 발생해야 한다.")
	void getAccountProductDetail_shouldThrowExceptionWhenNotFound() {
		when(accountProductRepository.existsAccountProductById(99L)).thenReturn(false);

		BadRequestException exception = assertThrows(
				BadRequestException.class,
				() -> accountProductService.getAccountProductDetail(99L)
		);
		System.out.println(exception.getCode() + " : " + exception.getMessage());
		assertThat(exception.getCode()).isEqualTo(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND.getCode());
	}

	@Test
	@DisplayName("계좌 상품을 생성할 수 있다.")
	void registerAccountProduct_shouldCreateProduct() {
		// given
		AccountProductCreationRequest request = new AccountProductCreationRequest(
				"New Account",
				"Special benefits",
				1L,
				0.1,
				"Standard Rate",
				1,
				12,
				1000L,
				100000L
		);
		AccountProduct savedProduct = request.toEntity();

		Bank mockBank = Bank.createBank("Mock Bank", "https://logo.mockbank.com");
		given(bankRepository.findById(request.bankId())).willReturn(Optional.of(mockBank));
		when(accountProductRepository.saveAccountProduct(any(AccountProduct.class)))
				.thenReturn(savedProduct);

		// when
		AccountProductSummary result = accountProductService.registerAccountProduct(request);

		// then
		assertThat(result.accountProductName()).isEqualTo("New Account");
	}

	@Test
	@DisplayName("자유입출금 상품 목록을 조회할 수 있다.")
	void getDemandProducts_shouldReturnDemandList() {
		// given
		List<AccountProductSummary> mockList = List.of(
				new AccountProductSummary(
						1L,
						"자유입출금",
						1L,
						"샘플 은행",
						"https://logo.mockbank.com/logo.png",
						0.5,
						null,
						0L,
						0L,
						AccountProductType.DEMAND.name())
		);

		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, mockList, false);

		when(accountProductRepository.getPaginatedAccountProductsByAccountProductType(
				AccountProductType.DEMAND, null))
				.thenReturn(expectedPage);

		// when
		PageInfo<AccountProductSummary> result = accountProductService.getDemandProducts(null);

		// then
		assertThat(result.data()).hasSize(1);
		assertThat(result.data().get(0).accountProductType())
				.isEqualTo(AccountProductType.DEMAND.name());
	}

	@Test
	@DisplayName("예금 상품 목록을 조회할 수 있다.")
	void getDepositProducts_shouldReturnDepositList() {
		// given
		List<AccountProductSummary> mockList = List.of(
				new AccountProductSummary(
						2L,
						"예금 상품",
						1L,
						"샘플 은행",
						"https://logo.mockbank.com/logo.png",
						2.0,
						12,
						1000L,
						100000L,
						AccountProductType.DEPOSIT.name())
		);

		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, mockList, false);

		when(accountProductRepository.getPaginatedAccountProductsByAccountProductType(
				AccountProductType.DEPOSIT, null))
				.thenReturn(expectedPage);

		// when
		PageInfo<AccountProductSummary> result = accountProductService.getDepositProducts(null);

		// then
		assertThat(result.data()).hasSize(1);
		assertThat(result.data().get(0).accountProductType())
				.isEqualTo(AccountProductType.DEPOSIT.name());
	}

	@Test
	@DisplayName("적금 상품 목록을 조회할 수 있다.")
	void getInstallmentProducts_shouldReturnInstallmentList() {
		// given
		List<AccountProductSummary> mockList = List.of(
				new AccountProductSummary(
						3L,
						"적금 상품",
						1L,
						"샘플 은행",
						"https://logo.mockbank.com/logo.png",
						3.0,
						12,
						500L,
						50000L,
						AccountProductType.INSTALLMENT.name())
		);

		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, mockList, false);

		when(accountProductRepository.getPaginatedAccountProductsByAccountProductType(
				AccountProductType.INSTALLMENT, null))
				.thenReturn(expectedPage);

		// when
		PageInfo<AccountProductSummary> result = accountProductService.getInstallmentProducts(null);

		// then
		assertThat(result.data()).hasSize(1);
		assertThat(result.data().get(0).accountProductType())
				.isEqualTo(AccountProductType.INSTALLMENT.name());
	}
}
