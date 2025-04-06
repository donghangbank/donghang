package bank.donghang.core.accountproduct.application;

import java.util.List;

import org.springframework.stereotype.Service;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountProductService {
	private final AccountProductRepository accountProductRepository;

	public List<AccountProductSummary> getAllAccountProducts() {
		return accountProductRepository.getAccountProductsByQueryDsl(null);
	}

	public List<AccountProductSummary> getDemandProducts() {
		return accountProductRepository.getAccountProductsByQueryDsl(AccountProductType.DEMAND);
	}

	public List<AccountProductSummary> getDepositProducts() {
		return accountProductRepository.getAccountProductsByQueryDsl(AccountProductType.DEPOSIT);
	}

	public List<AccountProductSummary> getInstallmentProducts() {
		return accountProductRepository.getAccountProductsByQueryDsl(AccountProductType.INSTALLMENT);
	}

	public AccountProductDetail getAccountProductDetail(Long id) {
		if (!accountProductRepository.existsAccountProductById(id)) {
			throw new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND);
		}

		AccountProduct accountProduct = accountProductRepository.getAccountProductById(id);
		return AccountProductDetail.from(accountProduct);
	}

	public AccountProductSummary registerAccountProduct(AccountProductCreationRequest request) {

		AccountProduct accountProduct = accountProductRepository.saveAccountProduct(
			request.toEntity());
		return AccountProductSummary.from(accountProduct);
	}
}
