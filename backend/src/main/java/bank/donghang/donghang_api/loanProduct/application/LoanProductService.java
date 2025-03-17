package bank.donghang.donghang_api.loanProduct.application;

import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import bank.donghang.donghang_api.loanProduct.domain.LoanProduct;
import bank.donghang.donghang_api.loanProduct.domain.enums.LoanType;
import bank.donghang.donghang_api.loanProduct.domain.repository.LoanProductRepository;
import bank.donghang.donghang_api.loanProduct.dto.request.LoanProductCreateRequest;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductDetailResponse;
import bank.donghang.donghang_api.loanProduct.dto.response.LoanProductSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        checkLoanProductExistenc(id);

        return loanProductRepository.getLoanProductDetail(id);
    }

    public List<LoanProductSummaryResponse> getLoanProductSummaries(LoanType loanType){
        return loanProductRepository.getLoanProductSummaries(loanType);
    }

    private void checkLoanProductExistenc(Long id) {
        if (!loanProductRepository.existsById(id)) {
            throw new BadRequestException(ErrorCode.LOAN_PRODUCT_NOT_FOUND);
        }
    }
}
