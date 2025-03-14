package bank.donghang.donghang_api.loanProduct.application;

import bank.donghang.donghang_api.loanProduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanProduct.domain.repository.LoanProductRepository;
import bank.donghang.donghang_api.loanProduct.dto.request.LoanProductCreateRequest;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoanProductService {

    private final LoanProductRepository loanProductRepository;

    @Transactional
    public Long createLoanProduct(LoanProductCreateRequest request){

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

    public LoanProductDetailResponse getLoanProductDetail(Long id){

    }
}
