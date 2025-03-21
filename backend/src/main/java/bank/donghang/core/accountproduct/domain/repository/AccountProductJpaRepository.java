package bank.donghang.core.accountproduct.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.donghang.core.accountproduct.domain.AccountProduct;

@Repository
public interface AccountProductJpaRepository extends JpaRepository<AccountProduct, Long> {
	AccountProduct findAccountProductByAccountProductId(Long productId);

	boolean existsAccountProductByAccountProductId(Long productId);
}
