package bank.donghang.core.loanproduct.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bank.donghang.core.loanproduct.domain.LoanProduct;
import bank.donghang.core.loanproduct.domain.enums.LoanType;
import bank.donghang.core.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.core.loanproduct.dto.response.LoanProductSummaryResponse;
import lombok.RequiredArgsConstructor;

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
