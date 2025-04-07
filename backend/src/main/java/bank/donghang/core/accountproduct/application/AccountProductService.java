package bank.donghang.core.accountproduct.application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.accountproduct.dto.request.AccountProductCreationRequest;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.bank.domain.Bank;
import bank.donghang.core.bank.domain.repository.BankRepository;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountProductService {
	private final AccountProductRepository accountProductRepository;
	private final BankRepository bankRepository;

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
		Optional<Bank> optBank = bankRepository.findById(request.bankId());
		if (optBank.isEmpty()) {
			throw new BadRequestException(ErrorCode.BANK_NOT_FOUND);
		}

		Bank bank = optBank.get();

		AccountProduct accountProduct = accountProductRepository.saveAccountProduct(
			request.toEntity());

		return AccountProductSummary.from(accountProduct, bank.getName(), bank.getLogoUrl());
	}
}
