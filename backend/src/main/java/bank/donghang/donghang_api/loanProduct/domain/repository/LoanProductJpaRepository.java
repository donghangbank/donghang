package bank.donghang.donghang_api.loanProduct.domain.repository;

import bank.donghang.donghang_api.loanProduct.domain.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanProductJpaRepository extends JpaRepository<LoanProduct, Long>, LoanProductJpaRepositoryCustom {
}
