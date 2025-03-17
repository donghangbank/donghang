package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<LoanProductSummaryResponse> getLoanProductSummaries(LoanType loanType) {
        return loanProductJpaRepository.getLoanProductSummaries(loanType);
    }

    public boolean existsById(Long loanProductId) {
        return loanProductJpaRepository.existsById(loanProductId);
    }
}
