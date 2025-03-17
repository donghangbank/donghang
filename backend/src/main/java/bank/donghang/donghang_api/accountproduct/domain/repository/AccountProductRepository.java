package bank.donghang.donghang_api.accountproduct.domain.repository;

import bank.donghang.donghang_api.accountproduct.domain.AccountProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountProductRepository {
    private final AccountProductJPARepository accountProductJPARepository;

    public List<AccountProduct> getAccountProducts() {
        return accountProductJPARepository.findAll();
    }

    public Optional<AccountProduct> getAccountProductById(Long id) {
        return accountProductJPARepository.findAccountProductByAccountProductId(id);
    }

    public AccountProduct saveAccountProduct(AccountProduct accountProduct) {
        return accountProductJPARepository.save(accountProduct);
    }
}
