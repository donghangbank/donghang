package bank.donghang.donghang_api.loanProduct.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanJpaRepository extends JpaRepository<LoanProduct, Long> {
}
