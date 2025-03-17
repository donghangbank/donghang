package bank.donghang.donghang_api.loanproduct.domain.repository;

import bank.donghang.donghang_api.loanproduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanproduct.dto.response.LoanProductSummaryResponse;

import java.util.List;

public interface LoanProductJpaRepositoryCustom {

    LoanProductDetailResponse getLoanProductDetailResponse(Long loanProductId);

    List<LoanProductSummaryResponse> getLoanProductSummaries(LoanType loanType);
}
