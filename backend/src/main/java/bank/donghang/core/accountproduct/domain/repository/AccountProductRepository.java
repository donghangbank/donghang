package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.common.dto.PageInfo;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountProductRepository {
	private static final int ACCOUNT_PRODUCT_PAGE_SIZE = 4;

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

	public PageInfo<AccountProductSummary> getPaginatedAccountProductsByAccountProductType(
		AccountProductType accountProductType,
		String pageToken
	) {
		var data =  accountProductJpaRepositoryCustomImpl.getAccountProductsByAccountProductType(
			accountProductType,
			pageToken,
			ACCOUNT_PRODUCT_PAGE_SIZE
		);

		if (data.size() <= ACCOUNT_PRODUCT_PAGE_SIZE) {
			return PageInfo.of(
				null,
				data,
				false
			);
		}

		var lastData = data.get(data.size() - 1);
		data.remove(data.size() - 1);
		String nextPageToken = String.valueOf(lastData.accountProductId());

		return PageInfo.of(nextPageToken, data, true);
	}
}
