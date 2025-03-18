package bank.donghang.core.cardproduct.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpMethod.*;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.cardproduct.application.CardProductService;
import bank.donghang.core.cardproduct.domain.enums.CardDuration;
import bank.donghang.core.cardproduct.domain.enums.CardProductType;
import bank.donghang.core.cardproduct.dto.request.CardProductCreateRequest;
import bank.donghang.core.cardproduct.dto.request.CardProductUpdateRequest;
import bank.donghang.core.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.core.cardproduct.dto.response.CardProductSummaryResponse;
import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.s3.application.S3FileService;

@WebMvcTest(CardProductController.class)
class CardProductControllerTest extends ControllerTest {

	@Autowired
	ObjectMapper objectMapper;
	@MockitoBean
	private CardProductService cardProductService;
	@MockitoBean
	private S3FileService s3FileService;

	@Test
	@DisplayName("카드 상품을 생성할 수 있다.")
	public void create_card_product() throws Exception {

		Long companyId = 1L;
		String imageUrl = "https://test-bucket.s3.region.amazonaws.com/cardproduct/test.jpg";

		var request = new CardProductCreateRequest(
			"삼성신용카드",
			"이자율이 100프로!!",
			CardProductType.CREDIT,
			companyId,
			CardDuration.YEAR
		);

		MockMultipartFile image = new MockMultipartFile(
			"image",
			"test.jpg",
			"image/jpeg",
			"test image content".getBytes()
		);

		MockMultipartFile requestPart = new MockMultipartFile(
			"request",
			"",
			"application/json",
			objectMapper.writeValueAsBytes(request)
		);

		given(s3FileService.uploadFileToS3(any(), eq("cardproduct")))
			.willReturn(imageUrl);
		given(cardProductService.createCardProduct(any(), any()))
			.willReturn(1L);

		mockMvc.perform(multipart("/api/v1/cardproducts")
				.file(requestPart)
				.file(image)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("카드 상품 정보를 조회할 수 있다.")
	public void find_card_product_detail() throws Exception {

		Long cardProductId = 1L;

		var expect = new CardProductDetailResponse(
			1L,
			"삼성신용카드",
			CardProductType.CREDIT,
			"www.test.com",
			"꼭 가입하세요.",
			"삼성카드",
			"www.testLogo.com",
			CardDuration.YEAR
		);

		given(cardProductService.getCardProductDetail(any()))
			.willReturn(expect);

		MvcResult result = mockMvc.perform(get("/api/v1/cardproducts/" + cardProductId))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		var response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<CardProductDetailResponse>() {
			}
		);

		Assertions.assertThat(response).isEqualTo(expect);
	}

	@Test
	@DisplayName("카드 상품 목록을 조회할 수 있다.")
	public void find_card_product_summaries() throws Exception {

		var expect = List.of(
			new CardProductSummaryResponse(
				1L,
				"삼성신용카드",
				"www.test.com",
				CardProductType.CREDIT,
				"삼성카드",
				"www.testLogo.com"
			),
			new CardProductSummaryResponse(
				2L,
				"삼성체크카드",
				"www.test.com",
				CardProductType.DEBIT,
				"삼성카드",
				"www.testLogo.com"
			)
		);

		given(cardProductService.getCardProductSummaries(any(), any()))
			.willReturn(expect);

		MvcResult result = mockMvc.perform(get("/api/v1/cardproducts"))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		var response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<List<CardProductSummaryResponse>>() {
			}
		);

		Assertions.assertThat(response).isEqualTo(expect);
	}

	@Test
	@DisplayName("카드 상품 정보를 수정할 수 있다.")
	public void update_card_product() throws Exception {

		Long cardProductId = 1L;

		var request = new CardProductUpdateRequest(
			"종하 카드",
			"짱이에요",
			CardProductType.CREDIT,
			CardDuration.YEAR
		);

		MockMultipartFile image = new MockMultipartFile(
			"image",
			"logo.png",
			MediaType.IMAGE_PNG_VALUE,
			"dummy image content".getBytes()
		);

		MockMultipartFile requestPart = new MockMultipartFile(
			"request",
			"",
			MediaType.APPLICATION_JSON_VALUE,
			objectMapper.writeValueAsBytes(request)
		);

		doNothing().when(cardProductService).updateCardProduct(any(), any(), any());

		mockMvc.perform(multipart(PATCH, "/api/v1/cardproducts/" + cardProductId)
				.file(image)
				.file(requestPart)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(cardProductService).updateCardProduct(any(), any(), any());
	}

	@Test
	@DisplayName("카드사를 삭제할 수 있다")
	public void can_delete_card_company() throws Exception {
		Long cardProductId = 1L;

		doNothing().when(cardProductService).deleteCardProduct(any());

		mockMvc.perform(delete("/api/v1/cardproducts/" + cardProductId))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(cardProductService).deleteCardProduct(any());
	}
}
