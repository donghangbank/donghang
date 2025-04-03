package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountProductRepository {
	private final AccountProductJpaRepository accountProductJpaRepository;
	private final AccountProductJpaRepositoryCustomImpl accountProductJpaRepositoryCustomImpl;

	public List<AccountProduct> getAccountProducts() {
		return accountProductJpaRepository.findAll();
	}

	public AccountProduct getAccountProductById(Long id) {
		return accountProductJpaRepository.findAccountProductByAccountProductId(id);
	}

	public AccountProduct saveAccountProduct(AccountProduct accountProduct) {
		return accountProductJpaRepository.save(accountProduct);
	}

	public boolean existsAccountProductById(Long id) {
		return accountProductJpaRepository.existsAccountProductByAccountProductId(id);
	}

	public void deleteAll() {
		accountProductJpaRepository.deleteAll();
	}

	public List<AccountProductSummary> getAccountProductsByQueryDsl() {
		return accountProductJpaRepositoryCustomImpl.getAccountProductsByQueryDSL();
	}
}
