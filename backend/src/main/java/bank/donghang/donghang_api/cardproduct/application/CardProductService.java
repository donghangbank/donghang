package bank.donghang.donghang_api.cardproduct.application;

import bank.donghang.donghang_api.cardproduct.domain.CardProduct;
import bank.donghang.donghang_api.cardproduct.domain.repository.CardProductRepository;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductCreateRequest;
import bank.donghang.donghang_api.cardproduct.dto.response.CardProductDetailResponse;
import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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
        log.info("getCardProductDetail");
        if (!cardProductRepository.existsCardProduct(id)){
            throw new BadRequestException(ErrorCode.CARD_PRODUCT_NOT_FOUND);
        }

        CardProductDetailResponse response = cardProductRepository.findCardProductDetailById(id);

        return response;
    }
}
