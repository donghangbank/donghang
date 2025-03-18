package bank.donghang.core.loanproduct.domain.repository;

import java.util.List;

import bank.donghang.core.loanproduct.domain.enums.LoanType;
import bank.donghang.core.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.core.loanproduct.dto.response.LoanProductSummaryResponse;

public interface LoanProductJpaRepositoryCustom {

	LoanProductDetailResponse getLoanProductDetailResponse(Long loanProductId);

	List<LoanProductSummaryResponse> getLoanProductSummaries(LoanType loanType);
}
