package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoanProductRepository {

    private final LoanProductJpaRepository loanProductJpaRepository;

    public LoanProduct save(LoanProduct loanProduct) {
        return loanProductJpaRepository.save(loanProduct);
    }

    public LoanProductDetailResponse getLoanProductDetail(Long loanProductId) {
        return loanProductJpaRepository.getLoanProductDetailResponse(loanProductId);
    }

    public boolean existsById(Long loanProductId) {
        return loanProductJpaRepository.existsById(loanProductId);
    }
}
