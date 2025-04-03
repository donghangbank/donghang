package bank.donghang.core.accountproduct.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
				2.5,
				null,
				0L,
				0L,
				AccountProductType.DEMAND.name())
		);

		when(productService.getAllAccountProductsByQueryDsl()).thenReturn(summaries);

		MvcResult result = mockMvc.perform(get("/api/v1/accountproducts")
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		List<AccountProductSummary> response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<>() {}
		);

		Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(summaries);
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
}
