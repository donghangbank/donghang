package bank.donghang.core.accountproduct.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductListResponse;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountProductService {
	private final AccountProductRepository accountProductRepository;

	// todo. queryDSL 로 구현 예정
	public AccountProductListResponse getAllAccountProducts() {
		List<AccountProduct> accountProducts = accountProductRepository.getAccountProducts();
		List<AccountProductSummary> accountProductInfos = accountProducts.stream()
			.map(AccountProductSummary::from)
			.toList();

		return new AccountProductListResponse(accountProductInfos);
	}

	public AccountProductDetail getAccountProductDetail(Long id) {
		Optional<AccountProduct> optAccountProduct = accountProductRepository.getAccountProductById(id);
		if (optAccountProduct.isPresent()) {
			AccountProduct accountProduct = optAccountProduct.get();
			return AccountProductDetail.from(accountProduct);
		}

		throw new BadRequestException(ErrorCode.PRODUCT_NOT_FOUND);
	}

	public AccountProductSummary registerAccountProduct(
		AccountProductCreationRequest accountProductCreationRequestDto) {

		AccountProduct accountProduct = accountProductRepository.saveAccountProduct(
			accountProductCreationRequestDto.toEntity());
		return AccountProductSummary.from(accountProduct);
	}
}
