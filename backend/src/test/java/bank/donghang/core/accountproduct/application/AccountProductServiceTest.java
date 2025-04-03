package bank.donghang.core.accountproduct.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

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
import bank.donghang.core.accountproduct.dto.response.AccountProductListResponse;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AccountProductServiceTest {

	@InjectMocks
	private AccountProductService accountProductService;

	@Mock
	private AccountProductRepository accountProductRepository;

	@Test
	@DisplayName("계좌 상품 목록을 조회할 수 있다.")
	void getAllAccountProducts_shouldReturnProductList() {
		List<AccountProduct> mockProducts = List.of(
			AccountProduct.builder()
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
				.build(),
			AccountProduct.builder()
				.accountProductId(2L)
				.accountProductName("Checking Account")
				.accountProductDescription("Daily Transactions")
				.bankId(1L)
				.interestRate(0.2)
				.accountProductType(AccountProductType.DEMAND)
				.subscriptionPeriod(null)
				.rateDescription("기본 이율")
				.minSubscriptionBalance(0L)
				.maxSubscriptionBalance(0L)
				.build()
		);

		List<AccountProductSummary> mockSummaries = mockProducts.stream()
			.map(AccountProductSummary::from)
			.toList();

		when(accountProductRepository.getAccountProductsByQueryDSL()).thenReturn(mockSummaries);

		List<AccountProductSummary> response = accountProductService.getAllAccountProductsByQueryDSL();

		assertThat(response).hasSize(2);
		assertThat(response.get(0).accountProductName()).isEqualTo("Saving Account");
		assertThat(response.get(1).accountProductName()).isEqualTo("Checking Account");
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
		AccountProductCreationRequest request = new AccountProductCreationRequest(
			"New Account",
			"Special benefits",
			1L,
			0.1,
			"Standard Rate",
			1,
			12L,
			1000L,
			100000L
		);
		AccountProduct savedProduct = request.toEntity();

		when(accountProductRepository.saveAccountProduct(any(AccountProduct.class))).thenReturn(savedProduct);

		AccountProductSummary result = accountProductService.registerAccountProduct(request);

		assertThat(result.accountProductName()).isEqualTo("New Account");
	}
}
