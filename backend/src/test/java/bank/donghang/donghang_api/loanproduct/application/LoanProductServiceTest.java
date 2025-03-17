package bank.donghang.donghang_api.loanproduct.application;

import bank.donghang.donghang_api.common.enums.Period;
import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import bank.donghang.donghang_api.loanproduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.domain.enums.RepaymentMethod;
import bank.donghang.donghang_api.loanproduct.domain.repository.LoanProductRepository;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductCreateRequest;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductUpdateRequest;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductSummaryResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoanProductServiceTest {

    @InjectMocks
    private LoanProductService loanProductService;

    @Mock
    LoanProductRepository loanProductRepository;

    @Test
    @DisplayName("대출 상품을 생성할 수 있다.")
    public void can_create_loan_product() {

        Long loanProductId = 1L;

        var request = new LoanProductCreateRequest(
                1L,
                1L,
                "대출상품",
                Period.MONTH,
                LoanType.CREDIT,
                200000,
                200000,
                4.2,
                "베스트상품입니다.",
                RepaymentMethod.BALLOON_PAYMENT
        );

        LoanProduct loanProduct = LoanProduct.createLoanProduct(
                1L,
                1L,
                "입자",
                Period.MONTH,
                LoanType.CREDIT,
                200000,
                200000,
                4.2,
                "베스트상품입니다.",
                RepaymentMethod.BALLOON_PAYMENT
        );

        given(loanProductRepository.save(any(LoanProduct.class)))
                .willReturn(loanProduct);

        loanProductService.createLoanProduct(request);

        verify(loanProductRepository).save(any(LoanProduct.class));
    }

    @Test
    @DisplayName("대출 상품 상세 정보를 조회할 수 있다.")
    public void can_get_loan_product_detail() {

        Long loanProductId = 1L;
        LoanProductDetailResponse expectedResponse = new LoanProductDetailResponse(
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

        given(loanProductRepository.existsById(loanProductId))
                .willReturn(true);
        given(loanProductRepository.getLoanProductDetail(loanProductId))
                .willReturn(expectedResponse);

        LoanProductDetailResponse response = loanProductService.getLoanProductDetail(loanProductId);

        Assertions.assertThat(response).isNotNull();
        verify(loanProductRepository).existsById(loanProductId);
        verify(loanProductRepository).getLoanProductDetail(loanProductId);
    }

    @Test
    @DisplayName("대출 상품이 존재하지 않으면 예외가 발생한다.")
    public void throws_exception_when_card_product_not_exists() {

        Long loanProductId = 1L;
        given(loanProductRepository.existsById(loanProductId))
                .willReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> loanProductService.getLoanProductDetail(loanProductId));

        Assertions.assertThat(exception.getCode()).isEqualTo(ErrorCode.LOAN_PRODUCT_NOT_FOUND.getCode());
    }

    @Test
    @DisplayName("대출 상품 요약 정보 목록을 조회할 수 있다.")
    public void can_get_loan_product_summaries() {

        LoanType loanType = LoanType.CREDIT;

        List<LoanProductSummaryResponse> expectedResponse = List.of(
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
                        2L,
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

        given(loanProductRepository.getLoanProductSummaries(loanType))
                .willReturn(expectedResponse);

        List<LoanProductSummaryResponse> response = loanProductService.getLoanProductSummaries(loanType);

        Assertions.assertThat(response).hasSize(2);
        Assertions.assertThat(response.get(0).loanProductId()).isEqualTo(1L);
        Assertions.assertThat(response.get(1).loanProductId()).isEqualTo(2L);
        verify(loanProductRepository).getLoanProductSummaries(loanType);
    }

    @Test
    @DisplayName("대출 상품 정보를 업데이트할 수 있다.")
    public void can_update_loan_product() {

        Long loanProductId = 1L;

        LoanProductUpdateRequest request = new LoanProductUpdateRequest(
                1L,
                "아아아",
                Period.MONTH,
                200000,
                200000,
                5.2,
                "아주 좋아요"
        );

        LoanProduct existingLoanProduct = LoanProduct.createLoanProduct(
                1L,
                1L,
                "종하대출",
                Period.MONTH,
                LoanType.CREDIT,
                200000,
                200000,
                5.2,
                "아주 좋아요",
                RepaymentMethod.BALLOON_PAYMENT
        );

        given(loanProductRepository.existsById(loanProductId))
                .willReturn(true);
        given(loanProductRepository.findById(loanProductId))
                .willReturn(Optional.of(existingLoanProduct));

        loanProductService.updateLoanProduct(loanProductId, request);

        verify(loanProductRepository).existsById(loanProductId);
        verify(loanProductRepository).findById(loanProductId);
        Assertions.assertThat(existingLoanProduct.getName()).isEqualTo("아아아");
    }
}