package bank.donghang.donghang_api.accountproduct.domain.repository;

import bank.donghang.donghang_api.accountproduct.domain.AccountProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountProductJPARepository extends JpaRepository<AccountProduct, Long> {
    public Optional<AccountProduct> findAccountProductByAccountProductId(Long productId);
}
