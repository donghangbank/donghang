package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;

public interface LoanProductJpaRepositoryCustom {

    LoanProductDetailResponse getLoanProductDetailResponse(Long loanProductId);
}
