package bank.donghang.core.accountproduct.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.accountproduct.application.AccountProductService;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@WebMvcTest(AccountProductController.class)
class AccountProductControllerTest extends ControllerTest {

	@MockitoBean
	private AccountProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("전체 상품 목록을 조회한다.")
	public void get_products() throws Exception {
		List<AccountProductSummary> summaries = List.of(
			new AccountProductSummary(
				1L,
				"샘플 상품",
				1L,
				"샘플 은행",
				"https://logo.mockbank.com/logo.png",
				2.5,
				null,
				0L,
				0L,
				AccountProductType.DEMAND.name())
		);

		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, summaries, false);

		when(productService.getAllAccountProducts(any())).thenReturn(expectedPage);

		MvcResult result = mockMvc.perform(get("/api/v1/accountproducts")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		PageInfo<AccountProductSummary> response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<>() {
			}
		);

		Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expectedPage);
	}

	@Test
	@DisplayName("상품 상세 정보를 조회한다.")
	public void get_product_detail() throws Exception {
		Long productId = 1L;
		AccountProductDetail expected = new AccountProductDetail(
			productId,
			"샘플 상품",
			"샘플 상품 설명",
			2L,
			2.5,
			"변동 금리",
			"저축 상품",
			100,
			12L,
			1000L,
			100000L
		);

		when(productService.getAccountProductDetail(productId)).thenReturn(expected);

		MvcResult result = mockMvc.perform(get("/api/v1/accountproducts/{productId}", productId)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		AccountProductDetail response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			AccountProductDetail.class
		);

		Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("상품을 생성한다.")
	public void create_product() throws Exception {
		AccountProductCreationRequest creationRequest = new AccountProductCreationRequest(
			"새 상품",
			"새 상품 설명",
			1L,
			2.5,
			"기본 금리",
			100,
			12L,
			1000L,
			100000L
		);

		AccountProductSummary expected = new AccountProductSummary(
			1L,
			"새 상품",
			1L,
			"샘플 은행",
			"https://logo.mockbank.com/logo.png",
			2.5,
			12L,
			1000L,
			100000L,
			"수시입출금 계좌"
		);

		when(productService.registerAccountProduct(creationRequest)).thenReturn(expected);

		String jsonRequest = objectMapper.writeValueAsString(creationRequest);

		MvcResult result = mockMvc.perform(
				post("/api/v1/accountproducts")
					.content(jsonRequest)
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		AccountProductSummary response = objectMapper.readValue(result.getResponse().getContentAsString(),
			AccountProductSummary.class);

		Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expected);
	}

	@Test
	@DisplayName("자유입출금 상품 목록을 조회한다.")
	void getDemandProducts_shouldReturnDemandProducts() throws Exception {
		// given
		AccountProductSummary summary = new AccountProductSummary(
			1L,
			"자유입출금 상품",
			1L,
			"샘플 은행",
			"https://logo.mockbank.com/logo.png",
			1.5,
			null,
			0L,
			0L,
			AccountProductType.DEMAND.name()
		);
		List<AccountProductSummary> summaries = List.of(summary);
		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, summaries, false);

		when(productService.getDemandProducts(any())).thenReturn(expectedPage);

		// when & then
		MvcResult result = mockMvc.perform(get("/api/v1/accountproducts/demands")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		PageInfo<AccountProductSummary> response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<>() {
			}
		);

		Assertions.assertThat(response.getData().get(0).accountProductType())
			.isEqualTo(AccountProductType.DEMAND.name());
	}

	@Test
	@DisplayName("예금 상품 목록을 조회한다.")
	void getDepositProducts_shouldReturnDepositProducts() throws Exception {
		// given
		AccountProductSummary summary = new AccountProductSummary(
			2L,
			"예금 상품",
			1L,
			"샘플 은행",
			"https://logo.mockbank.com/logo.png",
			2.5,
			12L,
			1000L,
			100000L,
			AccountProductType.DEPOSIT.name()
		);
		List<AccountProductSummary> summaries = List.of(summary);
		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, summaries, false);

		when(productService.getDepositProducts(any())).thenReturn(expectedPage);

		// when & then
		MvcResult result = mockMvc.perform(get("/api/v1/accountproducts/deposits")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		PageInfo<AccountProductSummary> response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<>() {
			}
		);

		Assertions.assertThat(response.getData().get(0).accountProductType())
			.isEqualTo(AccountProductType.DEPOSIT.name());
	}

	@Test
	@DisplayName("적금 상품 목록을 조회한다.")
	void getInstallmentProducts_shouldReturnInstallmentProducts() throws Exception {
		// given
		AccountProductSummary summary = new AccountProductSummary(
			3L,
			"적금 상품",
			1L,
			"샘플 은행",
			"https://logo.mockbank.com/logo.png",
			3.5,
			12L,
			500L,
			50000L,
			AccountProductType.INSTALLMENT.name()
		);
		List<AccountProductSummary> summaries = List.of(summary);
		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, summaries, false);

		when(productService.getInstallmentProducts(any())).thenReturn(expectedPage);

		// when & then
		MvcResult result = mockMvc.perform(get("/api/v1/accountproducts/installments")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		PageInfo<AccountProductSummary> response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<>() {
			}
		);

		Assertions.assertThat(response.getData().get(0).accountProductType())
			.isEqualTo(AccountProductType.INSTALLMENT.name());
	}

	@Test
	@DisplayName("상품 목록이 없을 때 빈 배열을 반환한다.")
	void getProducts_whenNoProducts_shouldReturnEmptyList() throws Exception {
		// given
		PageInfo<AccountProductSummary> expectedPage = PageInfo.of(null, Collections.emptyList(), false);
		when(productService.getAllAccountProducts(any())).thenReturn(expectedPage);

		// when & then
		MvcResult result = mockMvc.perform(get("/api/v1/accountproducts")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		PageInfo<AccountProductSummary> response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<>() {
			}
		);

		Assertions.assertThat(response.getData()).isEmpty();
	}

	@Test
	@DisplayName("존재하지 않는 상품 ID로 조회 시 에러를 반환한다.")
	void getProductDetail_withInvalidId_shouldReturnNotFound() throws Exception {
		// given
		Long invalidProductId = 999L;
		when(productService.getAccountProductDetail(invalidProductId))
			.thenThrow(new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND));

		// when & then
		mockMvc.perform(get("/api/v1/accountproducts/{productId}", invalidProductId)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("유효하지 않은 상품 생성 요청 시 에러를 반환한다.")
	void createProduct_withInvalidRequest_shouldReturnBadRequest() throws Exception {
		// given
		String invalidRequestJson = """
			{
			"accountProductName": "",
			"accountProductDescription": "",
			"bankId": null,
			"interestRate": -1.0,
			"rateDescription": "",
			"accountProductTypeCode": -1,
			"subscriptionPeriod": null,
			"minSubscriptionBalance": -1,
			"maxSubscriptionBalance": -1
			}
			""";

		mockMvc.perform(post("/api/v1/accountproducts")
				.content(invalidRequestJson)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("상품 생성 시 서비스 계층에서 예외가 발생하면 해당 예외를 처리한다.")
	void createProduct_whenServiceThrowsException_shouldHandleException() throws Exception {
		// given
		AccountProductCreationRequest request = new AccountProductCreationRequest(
			"유효한 상품",
			"유효한 설명",
			1L,
			2.5,
			"기본 금리",
			20,
			12L,
			1000L,
			100000L
		);

		when(productService.registerAccountProduct(request))
			.thenThrow(new BadRequestException(ErrorCode.BANK_NOT_FOUND));

		// when & then
		mockMvc.perform(post("/api/v1/accountproducts")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isNotFound());
	}
}
