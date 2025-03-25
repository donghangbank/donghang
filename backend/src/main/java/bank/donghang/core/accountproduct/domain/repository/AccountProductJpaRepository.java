package bank.donghang.core.accountproduct.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.accountproduct.domain.AccountProduct;

public interface AccountProductJpaRepository extends JpaRepository<AccountProduct, Long> {
	AccountProduct findAccountProductByAccountProductId(Long productId);

	boolean existsAccountProductByAccountProductId(Long productId);
}
