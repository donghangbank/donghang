package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductSummaryResponse;

import java.util.List;

public interface LoanProductJpaRepositoryCustom {

    LoanProductDetailResponse getLoanProductDetailResponse(Long loanProductId);

    List<LoanProductSummaryResponse> getLoanProductSummaries(LoanType loanType);
}
