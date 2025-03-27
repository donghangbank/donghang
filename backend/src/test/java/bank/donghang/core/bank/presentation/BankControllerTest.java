package bank.donghang.core.bank.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
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

import bank.donghang.core.bank.application.BankService;
import bank.donghang.core.bank.domain.Bank;
import bank.donghang.core.bank.dto.request.BankRequest;
import bank.donghang.core.cardcompany.dto.request.CardCompanyRequest;
import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.s3.application.S3FileService;

@WebMvcTest(BankController.class)
class BankControllerTest extends ControllerTest {

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	private BankService bankService;
	@MockitoBean
	private S3FileService s3FileService;

	@Test
	@DisplayName("은행을 생성할 수 있다.")
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

		given(s3FileService.uploadFileToS3(any(), eq("bank")))
			.willReturn(logoUrl);
		given(bankService.createBank(any(), any()))
			.willReturn(1L);

		mockMvc.perform(multipart("/api/v1/banks")
				.file(requestPart)
				.file(image)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isCreated());

	}

	@Test
	@DisplayName("은행 목록을 조회할 수 있다.")
	public void can_find_all_banks() throws Exception {

		var expect = List.of(
			Bank.createBank(
				"삼성은행",
				"www.test.com"
			),
			Bank.createBank(
				"종하은행",
				"www.test.com"
			),
			Bank.createBank(
				"종우은행",
				"www.test.com"
			)
		);

		given(bankService.getAllBanks())
			.willReturn(expect);

		MvcResult result = mockMvc.perform(get("/api/v1/banks"))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		var response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<List<Bank>>() {
			}
		);

		Assertions.assertThat(response)
			.extracting(Bank::getName, Bank::getLogoUrl)
			.containsExactlyInAnyOrder(
				Tuple.tuple("삼성은행", "www.test.com"),
				Tuple.tuple("종하은행", "www.test.com"),
				Tuple.tuple("종우은행", "www.test.com")
			);
	}

	@Test
	@DisplayName("은행을 수정할 수 있다.")
	public void can_update_bank() throws Exception {

		Long bankId = 1L;

		var request = new BankRequest("종하은행");

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

		doNothing().when(bankService).updateBank(any(), any(), any());

		mockMvc.perform(multipart(PATCH, "/api/v1/banks/{bankId}", bankId)
				.file(image)
				.file(requestPart)
				.contentType(MediaType.MULTIPART_FORM_DATA))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(bankService).updateBank(any(), any(), any());
	}

	@Test
	@DisplayName("은행을 삭제할 수 있다")
	public void can_delete_bank() throws Exception {
		Long bankId = 1L;

		doNothing().when(bankService).deleteBank(any(Long.class));

		mockMvc.perform(delete("/api/v1/banks/{bankId}", bankId))
			.andDo(print())
			.andExpect(status().isNoContent());

		verify(bankService).deleteBank(eq(bankId));
	}
}
