package bank.donghang.donghang_api.cardcompany.presentation;


import bank.donghang.donghang_api.cardcompany.application.CardCompanyService;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyCreateRequest;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyUpdateRequest;
import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;
import bank.donghang.donghang_api.common.controller.ControllerTest;
import bank.donghang.donghang_api.s3.application.S3FileService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(CardCompanyController.class)
class CardCompanyControllerTest extends ControllerTest {

    @MockitoBean
    private CardCompanyService cardCompanyService;

    @MockitoBean
    private S3FileService s3FileService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("카드 회사를 생성할 수 있다.")
    public void create_card_company() throws Exception {

        String logoUrl = "https://test-bucket.s3.region.amazonaws.com/logo/test.jpg";
        var request = new CardCompanyCreateRequest("삼성카드");

        MockMultipartFile image = new MockMultipartFile(
                "images",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        MockMultipartFile requestPart = new MockMultipartFile(
                "postCreateRequest",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(request)
        );

        given(s3FileService.uploadFileToS3(any(), eq("logo"))).willReturn(logoUrl);
        given(cardCompanyService.createCardCompany(any(), any())).willReturn(1L);

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

        given(cardCompanyService.findAllCardCompanies())
                .willReturn(expect);

        MvcResult result = mockMvc.perform(get("/api/v1/cardcompanies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<CardCompanySummaryResponse>>() {}
        );

        Assertions.assertThat(response).isEqualTo(expect);
    }

    @Test
    @DisplayName("카드사를 수정할 수 있다.")
    public void can_update_card_company() throws Exception {
        var request = new CardCompanyUpdateRequest(
                "종하카드",
                "www.test.com"
        );

        Long cardCompanyId = 1L;

        doNothing().when(cardCompanyService).updateCardCompany(any(),any());

        mockMvc.perform(patch("/api/v1/cardcompanies/" + cardCompanyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(cardCompanyService).updateCardCompany(any(),any());
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