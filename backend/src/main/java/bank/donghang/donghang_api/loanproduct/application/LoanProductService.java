package bank.donghang.donghang_api.loanproduct.application;

import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import bank.donghang.donghang_api.loanproduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.domain.repository.LoanProductRepository;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductCreateRequest;
import bank.donghang.donghang_api.loanproduct.dto.request.LoanProductUpdateRequest;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductSummaryResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanProductService {

	private final LoanProductRepository loanProductRepository;

	public Long createLoanProduct(LoanProductCreateRequest request) {

		LoanProduct loanProduct = LoanProduct.createLoanProduct(
			request.bankId(),
			request.ratingId(),
			request.name(),
			request.period(),
			request.type(),
			request.minLoanBalance(),
			request.maxLoanBalance(),
			request.interestRate(),
			request.description(),
			request.repaymentMethod()
		);

		return loanProductRepository.save(loanProduct).getId();
	}

	public LoanProductDetailResponse getLoanProductDetail(Long id) {

		checkLoanProductExistence(id);

		return loanProductRepository.getLoanProductDetail(id);
	}

	public List<LoanProductSummaryResponse> getLoanProductSummaries(LoanType loanType) {
		return loanProductRepository.getLoanProductSummaries(loanType);
	}

	@Transactional
	public void updateLoanProduct(
		Long id,
		LoanProductUpdateRequest request
	) {

		checkLoanProductExistence(id);

		LoanProduct loanProduct = loanProductRepository.findById(id)
			.orElseThrow(() -> new BadRequestException(ErrorCode.LOAN_PRODUCT_NOT_FOUND));

		loanProduct.updateLoanProduct(
			request.ratingId(),
			request.name(),
			request.period(),
			request.minLoanBalance(),
			request.maxLoanBalance(),
			request.interestRate(),
			request.description()
		);
	}

	public void deleteLoanProduct(Long id) {

		checkLoanProductExistence(id);
		loanProductRepository.deleteLoanProduct(id);
	}

	private void checkLoanProductExistence(Long id) {
		if (!loanProductRepository.existsById(id)) {
			throw new BadRequestException(ErrorCode.LOAN_PRODUCT_NOT_FOUND);
		}
	}
}
