package bank.donghang.core.loanproduct.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.loanproduct.domain.LoanProduct;

public interface LoanProductJpaRepository extends JpaRepository<LoanProduct, Long>, LoanProductJpaRepositoryCustom {
}
