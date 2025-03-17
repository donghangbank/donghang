package bank.donghang.donghang_api.loanproduct.presentation;

import bank.donghang.donghang_api.common.controller.ControllerTest;
import bank.donghang.donghang_api.common.enums.Period;
import bank.donghang.donghang_api.loanproduct.application.LoanProductService;
import bank.donghang.donghang_api.loanproduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.domain.enums.RepaymentMethod;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductCreateRequest;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductUpdateRequest;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductSummaryResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(LoanProductController.class)
class LoanProductControllerTest extends ControllerTest {

    @MockitoBean
    private LoanProductService loanProductService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("대출 상품을 생성할 수 있다.")
    public void create_loan_product() throws Exception {

        Long bankId = 1L;

        var request = new LoanProductCreateRequest(
                bankId,
                1L,
                "좋은대출상품",
                Period.MONTH,
                LoanType.CREDIT,
                200000,
                200000,
                4.2,
                "주택 구입을 위한 대출 상품",
                RepaymentMethod.BALLOON_PAYMENT
        );

        given(loanProductService.createLoanProduct(any()))
            .willReturn(1L);

        mockMvc.perform(post("/api/v1/loanproducts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("대출 상품 목록을 조회할 수 있다.")
    public void can_get_loan_product_summaries() throws Exception {

        var expect = List.of(
                new LoanProductSummaryResponse(
                        1L,
                        1L,
                        "대충상품1",
                        "종하은행",
                        LoanType.CREDIT,
                        200000,
                        200000,
                        4.2,
                        RepaymentMethod.BALLOON_PAYMENT

                ),
                new LoanProductSummaryResponse(
                        1L,
                        1L,
                        "대충상품2",
                        "신하은행",
                        LoanType.CREDIT,
                        200000,
                        200000,
                        4.2,
                        RepaymentMethod.BALLOON_PAYMENT

                )
        );

        given(loanProductService.getLoanProductSummaries(any()))
                .willReturn(expect);

        MvcResult result = mockMvc.perform(get("/api/v1/loanproducts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<LoanProductSummaryResponse>>() {}
        );

        Assertions.assertThat(response).isEqualTo(expect);
    }

    @Test
    @DisplayName("대충 상품 정보를 조회할 수 있다.")
    public void get_loan_product_detail() throws Exception {

        Long loanProductId = 1L;

        var expect = new LoanProductDetailResponse(
                loanProductId,
                1L,
                "베스트대출",
                "종하은행",
                Period.MONTH,
                LoanType.CREDIT,
                200000,
                200000,
                5.2,
                "아주 좋아요",
                RepaymentMethod.BALLOON_PAYMENT
        );

        given(loanProductService.getLoanProductDetail(any()))
            .willReturn(expect);

        MvcResult result = mockMvc.perform(get("/api/v1/loanproducts/" + loanProductId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<LoanProductDetailResponse>() {}
        );

        Assertions.assertThat(response).isEqualTo(expect);
    }

    @Test
    @DisplayName("대충 상품 정보를 수정할 수 있다.")
    public void can_update_loan_product() throws Exception {

        Long loanProductId = 1L;

        var request = new LoanProductUpdateRequest(
                1L,
                "아아아",
                Period.MONTH,
                200000,
                200000,
                5.2,
                "아주 좋아요"
        );

        doNothing().when(loanProductService).updateLoanProduct(any(),any());

        mockMvc.perform(patch("/api/v1/loanproducts/" + loanProductId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(loanProductService).updateLoanProduct(any(),any());
    }

    @Test
    @DisplayName("대출 상품을 삭제할 수 있다.")
    public void can_delete_loan_product() throws Exception {

        Long loanProductId = 1L;

        doNothing().when(loanProductService).deleteLoanProduct(any());

        mockMvc.perform(delete(String.format("/api/v1/loanproducts/%s", loanProductId)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(loanProductService).deleteLoanProduct(any());
    }
}