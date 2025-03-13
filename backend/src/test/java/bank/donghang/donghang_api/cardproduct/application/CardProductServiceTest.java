package bank.donghang.donghang_api.cardproduct.application;

import bank.donghang.donghang_api.cardproduct.domain.CardProduct;
import bank.donghang.donghang_api.cardproduct.domain.enums.CardDuration;
import bank.donghang.donghang_api.cardproduct.domain.enums.CardProductType;
import bank.donghang.donghang_api.cardproduct.domain.repository.CardProductRepository;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductCreateRequest;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductUpdateRequest;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductSummaryResponse;
import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardProductServiceTest {

    @InjectMocks
    private CardProductService cardProductService;

    @Mock
    CardProductRepository cardProductRepository;

    @Test
    @DisplayName("카드 상품을 생성할 수 있다.")
    public void can_create_card_product() {

        Long cardCompanyId = 1L;

        var request = new CardProductCreateRequest(
                "신상카드",
                "아아아",
                CardProductType.CREDIT,
                cardCompanyId,
                CardDuration.YEAR
        );

        String imageUrl = "www.donghang.com";

        CardProduct cardProduct = CardProduct.createCardProduct(
                request.name(),
                request.type(),
                request.description(),
                imageUrl,
                CardDuration.YEAR,
                cardCompanyId
        );

        given(cardProductRepository.save(any(CardProduct.class)))
                .willReturn(cardProduct);

        cardProductService.createCardProduct(request, imageUrl);

        verify(cardProductRepository).save(any());
    }

    @Test
    @DisplayName("카드 상품 상세 정보를 조회할 수 있다.")
    public void can_get_card_product_detail() {

        Long cardProductId = 1L;
        CardProductDetailResponse expectedResponse = new CardProductDetailResponse(
                cardProductId,
                "신상카드",
                CardProductType.CREDIT,
                "www.donghang.com",
                "dkdkdkdk",
                "신한카드",
                "www.test.com",
                CardDuration.YEAR
        );

        given(cardProductRepository.existsCardProduct(cardProductId)).willReturn(true);
        given(cardProductRepository.findCardProductDetailById(cardProductId)).willReturn(expectedResponse);

        CardProductDetailResponse response = cardProductService.getCardProductDetail(cardProductId);

        Assertions.assertThat(response).isNotNull();
        verify(cardProductRepository).existsCardProduct(cardProductId);
        verify(cardProductRepository).findCardProductDetailById(cardProductId);
    }

    @Test
    @DisplayName("카드 상품이 존재하지 않으면 예외가 발생한다.")
    public void throws_exception_when_card_product_not_exists() {

        Long cardProductId = 1L;
        given(cardProductRepository.existsCardProduct(cardProductId)).willReturn(false);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> cardProductService.getCardProductDetail(cardProductId));

        Assertions.assertThat(exception.getCode()).isEqualTo(ErrorCode.CARD_PRODUCT_NOT_FOUND.getCode());
    }

    @Test
    @DisplayName("카드 상품 요약 정보 목록을 조회할 수 있다.")
    public void can_get_card_product_summaries() {

        CardProductType type = CardProductType.CREDIT;
        String cardCompanyName = "신한카드";

        List<CardProductSummaryResponse> expectedResponses = List.of(
                new CardProductSummaryResponse(
                        1L,
                        "신상카드1",
                        "www.donghang.com/1",
                        CardProductType.DEBIT,
                        "신한카드",
                        "www.test.com"
                ),
                new CardProductSummaryResponse(
                        2L,
                        "신상카드2",
                        "www.donghang.com/2",
                        CardProductType.DEBIT,
                        "신한카드",
                        "www.test.com"
                )
        );

        given(cardProductRepository.findCardProductSummaries(type, cardCompanyName))
                .willReturn(expectedResponses);

        List<CardProductSummaryResponse> responses = cardProductService.getCardProductSummaries(type, cardCompanyName);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).cardProductId()).isEqualTo(1L);
        assertThat(responses.get(1).cardProductId()).isEqualTo(2L);
        verify(cardProductRepository).findCardProductSummaries(type, cardCompanyName);
    }

    @Test
    @DisplayName("카드 상품 정보를 업데이트할 수 있다.")
    public void can_update_card_product() {
        Long cardProductId = 1L;
        String newImageUrl = "www.donghang.com/new";
        CardProductUpdateRequest request = new CardProductUpdateRequest(
                "업데이트카드",
                "업데이트 설명",
                CardProductType.DEBIT,
                CardDuration.MONTH
        );

        CardProduct existingCardProduct = CardProduct.createCardProduct(
                "신상카드",
                CardProductType.CREDIT,
                "아아아",
                "www.donghang.com/old",
                CardDuration.YEAR,
                1L
        );

        given(cardProductRepository.existsCardProduct(cardProductId))
                .willReturn(true);
        given(cardProductRepository.findCardProductById(cardProductId))
                .willReturn(Optional.of(existingCardProduct));

        cardProductService.updateCardProduct(cardProductId, newImageUrl, request);

        verify(cardProductRepository).existsCardProduct(cardProductId);
        verify(cardProductRepository).findCardProductById(cardProductId);
        assertThat(existingCardProduct.getName()).isEqualTo("업데이트카드");
        assertThat(existingCardProduct.getDescription()).isEqualTo("업데이트 설명");
        assertThat(existingCardProduct.getType()).isEqualTo(CardProductType.DEBIT);
        assertThat(existingCardProduct.getImageUrl()).isEqualTo(newImageUrl);
        assertThat(existingCardProduct.getDuration()).isEqualTo(CardDuration.MONTH);
    }

    @Test
    @DisplayName("카드 상품을 삭제할 수 있다.")
    public void can_delete_card_product() {

        Long cardProductId = 1L;
        given(cardProductRepository.existsCardProduct(cardProductId))
                .willReturn(true);

        cardProductService.deleteCardProduct(cardProductId);

        verify(cardProductRepository).existsCardProduct(cardProductId);
        verify(cardProductRepository).deleteCardProduct(cardProductId);
    }
}