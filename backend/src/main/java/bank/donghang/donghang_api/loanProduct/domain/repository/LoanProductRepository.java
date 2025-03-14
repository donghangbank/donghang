package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.domain.LoanProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoanProductRepository {

    private final LoanProductJpaRepository loanProductJpaRepository;

    public LoanProduct save(LoanProduct loanProduct) {
        return loanProductJpaRepository.save(loanProduct);
    }
}
