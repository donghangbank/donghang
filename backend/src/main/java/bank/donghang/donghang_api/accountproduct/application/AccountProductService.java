package bank.donghang.donghang_api.accountproduct.application;

import bank.donghang.donghang_api.accountproduct.domain.AccountProduct;
import bank.donghang.donghang_api.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.donghang_api.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.donghang_api.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.donghang_api.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.donghang_api.accountproduct.dto.response.AccountProductListResponse;
import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountProductService {
    private final AccountProductRepository accountProductRepository;

    // todo. queryDSL 로 구현 예정
    public AccountProductListResponse getAllAccountProducts() {
        List<AccountProduct> accountProducts = accountProductRepository.getAccountProducts();
        List<AccountProductSummary> accountProductInfos = accountProducts.stream().map(AccountProductSummary::from).toList();

        return new AccountProductListResponse(accountProductInfos);
    }

    public AccountProductDetail getAccountProductDetail(Long id) {
        Optional<AccountProduct> OptAccountProduct = accountProductRepository.getAccountProductById(id);
        if (OptAccountProduct.isPresent()) {
            AccountProduct accountProduct = OptAccountProduct.get();
            return AccountProductDetail.from(accountProduct);
        }

        throw new BadRequestException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    @Transactional
    public AccountProductSummary registerAccountProduct(AccountProductCreationRequest accountProductCreationRequestDto) {

        AccountProduct accountProduct = accountProductRepository.saveAccountProduct(accountProductCreationRequestDto.toEntity());
        return AccountProductSummary.from(accountProduct);
    }
}
