package bank.donghang.donghang_api.loanproduct.domain.repository;

import bank.donghang.donghang_api.loanproduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public void deleteLoanProduct(Long loanProductId) {
        loanProductJpaRepository.deleteById(loanProductId);
    }

    public boolean existsById(Long loanProductId) {
        return loanProductJpaRepository.existsById(loanProductId);
    }

    public Optional<LoanProduct> findById(Long loanProductId) {
        return loanProductJpaRepository.findById(loanProductId);
    }
}
