package bank.donghang.donghang_api.cardproduct.application;

import bank.donghang.donghang_api.cardproduct.domain.CardProduct;
import bank.donghang.donghang_api.cardproduct.domain.repository.CardProductRepository;
import bank.donghang.donghang_api.cardproduct.dto.request.CardProductCreateRequest;
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
                imageUrl
        );

        return cardProductRepository.save(cardProduct).getId();
    }
}
