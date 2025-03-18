package bank.donghang.core.cardcompany.presentation;

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

import bank.donghang.core.cardcompany.application.CardCompanyService;
import bank.donghang.core.cardcompany.dto.request.CardCompanyRequest;
import bank.donghang.core.cardcompany.dto.response.CardCompanySummaryResponse;
import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.s3.application.S3FileService;

@WebMvcTest(CardCompanyController.class)
class CardCompanyControllerTest extends ControllerTest {

	@Autowired
	ObjectMapper objectMapper;
	@MockitoBean
	private CardCompanyService cardCompanyService;
	@MockitoBean
	private S3FileService s3FileService;

	@Test
	@DisplayName("카드 회사를 생성할 수 있다.")
	public void create_card_company() throws Exception {

		String logoUrl = "https://test-bucket.s3.region.amazonaws.com/logo/test.jpg";
		var request = new CardCompanyRequest("삼성카드");

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

		given(s3FileService.uploadFileToS3(any(), eq("logo")))
			.willReturn(logoUrl);
		given(cardCompanyService.createCardCompany(any(), any()))
			.willReturn(1L);

		mockMvc.perform(multipart("/api/v1/cardcompanies")
				.file(requestPart)
				.file(image)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("카드 회사 목록을 조회할 수 있다.")
	public void can_find_card_company_summaries() throws Exception {

		var expect = List.of(
			new CardCompanySummaryResponse(
				1L,
				"삼성카드",
				"www.test.com"
			),
			new CardCompanySummaryResponse(
				2L,
				"신한카드",
				"www.test.com"
			),
			new CardCompanySummaryResponse(
				3L,
				"국민카드",
				"www.test.com"
			)
		);

		given(cardCompanyService.getAllCardCompanies())
			.willReturn(expect);

		MvcResult result = mockMvc.perform(get("/api/v1/cardcompanies"))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		var response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<List<CardCompanySummaryResponse>>() {
			}
		);

		Assertions.assertThat(response).isEqualTo(expect);
	}

	@Test
	@DisplayName("카드사를 수정할 수 있다.")
	public void can_update_card_company() throws Exception {

		Long cardCompanyId = 1L;

		var request = new CardCompanyRequest("종하카드");

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

		doNothing().when(cardCompanyService).updateCardCompany(any(), any(), any());

		mockMvc.perform(multipart(PATCH, "/api/v1/cardcompanies/{cardCompanyId}", cardCompanyId)
				.file(image)
				.file(requestPart)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(cardCompanyService).updateCardCompany(any(), any(), any());
	}

	@Test
	@DisplayName("카드사를 삭제할 수 있다")
	public void can_delete_card_company() throws Exception {
		Long cardCompanyId = 1L;

		doNothing().when(cardCompanyService).deleteCardCompany(any());

		mockMvc.perform(delete("/api/v1/cardcompanies/" + cardCompanyId))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(cardCompanyService).deleteCardCompany(any());
	}
}
