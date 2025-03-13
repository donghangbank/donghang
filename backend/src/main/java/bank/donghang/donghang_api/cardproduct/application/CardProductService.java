package bank.donghang.donghang_api.cardproduct.application;

import bank.donghang.donghang_api.cardproduct.domain.CardProduct;
import bank.donghang.donghang_api.cardproduct.domain.repository.CardProductRepository;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductCreateRequest;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductUpdateRequest;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardProductService {

    private final CardProductRepository cardProductRepository;

    @Transactional
    public Long createCardProduct(
            CardProductCreateRequest request,
            String imageUrl
    ) {

        CardProduct cardProduct = CardProduct.createCardProduct(
                request.name(),
                request.type(),
                request.description(),
                imageUrl,
                request.duration(),
                request.cardCompanyId()
        );

        return cardProductRepository.save(cardProduct).getId();
    }

    public CardProductDetailResponse getCardProductDetail(Long id){
        checkCardProductExistence(id);

        CardProductDetailResponse response = cardProductRepository.findCardProductDetailById(id);

        return response;
    }

    @Transactional
    public void updateCardProduct(
            Long id,
            String newImageUrl,
            CardProductUpdateRequest request
    ) {
        checkCardProductExistence(id);

        CardProduct cardProduct = cardProductRepository.findCardProductById(id)
                .orElseThrow(()-> new BadRequestException(ErrorCode.CARD_PRODUCT_NOT_FOUND));

        cardProduct.updateCardCompany(
                request.name(),
                request.type(),
                request.description(),
                newImageUrl,
                request.duration()
        );
    }

    private void checkCardProductExistence(Long id) {
        if (!cardProductRepository.existsCardProduct(id)){
            throw new BadRequestException(ErrorCode.CARD_PRODUCT_NOT_FOUND);
        }
    }
}
