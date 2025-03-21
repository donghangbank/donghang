package bank.donghang.core.account.application;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final AccountProductRepository accountProductRepository;

	@Transactional
	public AccountRegisterResponse createDemandAccount(DemandAccountRegisterRequest demandAccountRegisterRequest) {
		Long productId = demandAccountRegisterRequest.accountProductId();

		if (!accountProductRepository.existsAccountProductById(productId)) {
			throw new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND);
		}

		AccountProduct accountProduct = accountProductRepository.getAccountProductById(productId);

		// todo. 은행, 상품 별 accountTypeCode 매핑
		String nextAccountNumber = accountRepository.getNextAccountNumber(
			"100",
			"001"
		);

		Account account = demandAccountRegisterRequest.toEntity(
			nextAccountNumber,
			accountProduct.getInterestRate()
		);

		Account savedAccount = accountRepository.saveAccount(account);

		return AccountRegisterResponse.from(savedAccount, accountProduct, null, null);
	}

	@Transactional
	public AccountRegisterResponse createDepositAccount(DepositAccountRegisterRequest depositAccountRegisterRequest) {

		if (!accountProductRepository.existsAccountProductById(depositAccountRegisterRequest.accountProductId())) {
			throw new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND);
		}

		AccountProduct accountProduct = accountProductRepository.getAccountProductById(
			depositAccountRegisterRequest.accountProductId());

		if (!accountProduct.isDepositProduct()) {
			throw new BadRequestException(ErrorCode.WRONG_ACCOUNT_PRODUCT_TYPE);
		}

		String withdrawalAccountNumber = depositAccountRegisterRequest.withdrawalAccountNumber();
		String payoutAccountNumber = depositAccountRegisterRequest.payoutAccountNumber();

		Optional<Account> optWithdrawalAccount = accountRepository.findAccountByFullAccountNumber(
			withdrawalAccountNumber);
		Optional<Account> optPayoutAccount = accountRepository.findAccountByFullAccountNumber(payoutAccountNumber);

		if (optWithdrawalAccount.isEmpty() || optPayoutAccount.isEmpty()) {
			throw new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND);
		}

		Account withdrawalAccount = optWithdrawalAccount.get();
		Account payoutAccount = optPayoutAccount.get();

		withdrawalAccount.verifyWithdrawalAccount(depositAccountRegisterRequest.memberId(),
			depositAccountRegisterRequest.initDepositAmount());
		payoutAccount.verifyPayoutAccount(depositAccountRegisterRequest.memberId());

		String newAccountNumber = accountRepository.getNextAccountNumber("200", "001");
		long subscriptionPeriod = accountProduct.getSubscriptionPeriod();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, (int)subscriptionPeriod);
		Date expiryDate = calendar.getTime();

		Account newDepositAccount = depositAccountRegisterRequest.toEntity(newAccountNumber,
			accountProduct.getInterestRate(),
			withdrawalAccount.getAccountId(), payoutAccount.getAccountId(), 0L, expiryDate);

		Account savedDepositAccount = accountRepository.saveAccount(newDepositAccount);

		// todo. withdrawal account에서 initialDepositAmount newDepositAccount로 송금

		return AccountRegisterResponse.from(savedDepositAccount, accountProduct, withdrawalAccountNumber,
			payoutAccountNumber);
	}
}
