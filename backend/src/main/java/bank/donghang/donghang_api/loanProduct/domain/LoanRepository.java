package bank.donghang.donghang_api.loanProduct.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoanRepository {

    private final LoanJpaRepository loanJpaRepository;
}
