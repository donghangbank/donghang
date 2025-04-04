package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;

public interface AccountProductJpaRepository extends JpaRepository<AccountProduct, Long> {
	AccountProduct findAccountProductByAccountProductId(Long productId);

	boolean existsAccountProductByAccountProductId(Long productId);

	List<AccountProduct> findAccountProductsByAccountProductType(AccountProductType type);
}
