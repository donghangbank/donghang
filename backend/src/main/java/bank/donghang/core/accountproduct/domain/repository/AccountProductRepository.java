package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountProductRepository {
	private final AccountProductJpaRepository accountProductJpaRepository;

	public List<AccountProduct> getAccountProducts() {
		return accountProductJpaRepository.findAll();
	}

	public Optional<AccountProduct> getAccountProductById(Long id) {
		return accountProductJpaRepository.findAccountProductByAccountProductId(id);
	}

	public AccountProduct saveAccountProduct(AccountProduct accountProduct) {
		return accountProductJpaRepository.save(accountProduct);
	}

	public AccountProduct getAccountProductByIdIfExist(Long id) {
		Optional<AccountProduct> optAccountProduct = accountProductJpaRepository.findAccountProductByAccountProductId(
			id);
		if (optAccountProduct.isEmpty()) {
			throw new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND);

		}

		return optAccountProduct.get();
	}
}
