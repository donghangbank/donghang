package bank.donghang.core.account.application;

import java.util.Optional;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
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

	public AccountRegisterResponse createDepositAccount(DepositAccountRegisterRequest req) {
		DepositInstallmentAccountData data = getDepositInstallmentAccountData(
			req.accountProductId(), req.withdrawalAccountNumber(), req.payoutAccountNumber()
		);

		if (!data.accountProduct.isDepositProduct()) {
			throw new BadRequestException(ErrorCode.WRONG_ACCOUNT_PRODUCT_TYPE);
		}

		data.withdrawalAccount.verifyWithdrawalAccount(req.memberId(), req.initDepositAmount());
		data.payoutAccount.verifyPayoutAccount(req.memberId());

		String newAccountNumber = accountRepository.getNextAccountNumber("200", "001");
		LocalDate expiryDate = LocalDate.now().plusMonths(data.accountProduct.getSubscriptionPeriod());

		Account newDepositAccount = req.toEntity(
			newAccountNumber,
			data.accountProduct.getInterestRate(),
			data.withdrawalAccount.getAccountId(),
			data.payoutAccount.getAccountId(),
			0L,
			expiryDate
		);

		Account savedDepositAccount = accountRepository.saveAccount(newDepositAccount);

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void afterCommit() {
				// todo. withdrawal 계좌에서 newDepositAccount로 송금 로직 추가
			}
		});

		return AccountRegisterResponse.from(
			savedDepositAccount,
			data.accountProduct,
			req.withdrawalAccountNumber(),
			req.payoutAccountNumber()
		);
	}

	public AccountRegisterResponse createInstallmentAccount(InstallmentAccountRegisterRequest req) {
		DepositInstallmentAccountData data = getDepositInstallmentAccountData(
			req.accountProductId(), req.withdrawalAccountNumber(), req.payoutAccountNumber()
		);

		data.withdrawalAccount.verifyWithdrawalAccount(req.memberId(), req.monthlyInstallmentAmount());
		data.payoutAccount.verifyPayoutAccount(req.memberId());

		String newAccountNumber = accountRepository.getNextAccountNumber("300", "001");
		LocalDate expiryDate = LocalDate.now().plusMonths(data.accountProduct.getSubscriptionPeriod());

		Account newInstallmentAccount = req.toEntity(
			newAccountNumber,
			data.accountProduct.getInterestRate(),
			data.withdrawalAccount.getAccountId(),
			data.payoutAccount.getAccountId(),
			expiryDate
		);

		Account savedInstallmentAccount = accountRepository.saveInstallmentAccount(newInstallmentAccount);

		return AccountRegisterResponse.from(
			savedInstallmentAccount,
			data.accountProduct,
			req.withdrawalAccountNumber(),
			req.payoutAccountNumber()
		);
	}

	private DepositInstallmentAccountData getDepositInstallmentAccountData(Long accountProductId, String withdrawalAccountNumber, String payoutAccountNumber) {
		if (!accountProductRepository.existsAccountProductById(accountProductId)) {
			throw new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_NOT_FOUND);
		}
		AccountProduct accountProduct = accountProductRepository.getAccountProductById(accountProductId);

		Optional<Account> optWithdrawalAccount = accountRepository.findAccountByFullAccountNumber(withdrawalAccountNumber);
		Optional<Account> optPayoutAccount = accountRepository.findAccountByFullAccountNumber(payoutAccountNumber);
		if (optWithdrawalAccount.isEmpty() || optPayoutAccount.isEmpty()) {
			throw new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND);
		}

		return new DepositInstallmentAccountData(accountProduct, optWithdrawalAccount.get(), optPayoutAccount.get());
	}

	private static class DepositInstallmentAccountData {
		final AccountProduct accountProduct;
		final Account withdrawalAccount;
		final Account payoutAccount;

		public DepositInstallmentAccountData(AccountProduct accountProduct, Account withdrawalAccount, Account payoutAccount) {
			this.accountProduct = accountProduct;
			this.withdrawalAccount = withdrawalAccount;
			this.payoutAccount = payoutAccount;
		}
	}
}
