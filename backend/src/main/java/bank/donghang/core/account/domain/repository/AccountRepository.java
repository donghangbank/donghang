package bank.donghang.core.account.domain.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.InstallmentSchedule;
import bank.donghang.core.account.domain.enums.InstallmentStatus;
import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import bank.donghang.core.account.dto.response.AccountPasswordResponse;
import bank.donghang.core.account.dto.response.AccountSummaryResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.common.dto.PageInfo;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
	private final AccountJpaRepository accountJpaRepository;
	private final InstallmentScheduleJpaRepository installmentScheduleJpaRepository;
	private final AccountJpaRepositoryCustomImpl accountJpaRepositoryCustomImpl;

	public PageInfo<AccountSummaryResponse> getMyAccounts(Long memberId, Long cursor) {
		return accountJpaRepositoryCustomImpl.getAccountSummaries(memberId, cursor);
	}

	@Transactional
	public Account saveAccount(Account account) {
		return accountJpaRepository.save(account);
	}

	// 적금 계좌 저장 + 적금 납입 스케줄 저장
	@Transactional
	public Account saveInstallmentAccount(
		Account account,
		AccountProduct accountProduct,
		LocalDate nextInstallmentScheduleDate
	) {
		Account savedAccount = saveAccount(account);

		InstallmentSchedule installmentSchedule =
				InstallmentSchedule
						.builder().installmentAccountId(savedAccount.getAccountId())
						.withdrawalAccountId(savedAccount.getWithdrawalAccountId())
						.installmentAmount(savedAccount.getMonthlyInstallmentAmount())
						.installmentSequence(1)
						.installmentStatus(InstallmentStatus.SCHEDULED)
						.installmentScheduledDate(nextInstallmentScheduleDate)
						.subscriptionPeriod(accountProduct.getSubscriptionPeriod())
						.build();

		installmentScheduleJpaRepository.save(installmentSchedule);
		return savedAccount;
	}

	public void saveInstallmentSchedule(InstallmentSchedule installmentSchedule) {
		installmentScheduleJpaRepository.save(installmentSchedule);
	}

	public List<InstallmentSchedule> findInstallmentScheduleByInstallmentDateAndScheduled(LocalDate today) {
		return installmentScheduleJpaRepository.findInstallmentScheduleByInstallmentScheduledDateAndInstallmentStatus(
				today, InstallmentStatus.SCHEDULED);
	}

	public void deleteAll() {
		accountJpaRepository.deleteAll();
	}

	/**
	 * (과목코드, 지점코드) 조합으로 다음 계좌번호를 생성한다.
	 * 예: 마지막 계좌번호가 000001이면 -> 000002
	 */
	public String getNextAccountNumber(String accountTypeCode, String branchCode) {
		// 1. 가장 큰 accountNumber를 가진 Account 조회
		Optional<Account> optAccount = accountJpaRepository
				.findTopByAccountTypeCodeAndBranchCodeOrderByAccountNumberDesc(accountTypeCode, branchCode);

		// 2. 값이 있으면 +1, 없으면 1부터 시작
		String nextNumber;
		if (optAccount.isPresent()) {
			String lastAccountNumber = optAccount.get().getAccountNumber(); // "000001"
			int parsedNumber = Integer.parseInt(lastAccountNumber);        // 1
			parsedNumber++;                                               // 2
			nextNumber = String.format("%06d", parsedNumber);             // "000002"
		} else {
			nextNumber = "000001";
		}

		return nextNumber;
	}

	public Optional<Account> findAccountById(Long accountId) {
		return accountJpaRepository.findById(accountId);
	}

	public Optional<Account> findAccountByFullAccountNumber(String fullAccountNumber) {
		String accountTypeCode = fullAccountNumber.substring(0, 3);
		String branchCode = fullAccountNumber.substring(3, 6);
		String accountNumber = fullAccountNumber.substring(6);

		return accountJpaRepository.findByAccountTypeCodeAndBranchCodeAndAccountNumber(accountTypeCode, branchCode,
				accountNumber);
	}

	public void deleteAllAccounts() {
		accountJpaRepository.deleteAll();
	}

	public void delete(Account account) {
		accountJpaRepository.delete(account);
	}

	public BalanceResponse getAccountBalance(String fullAccountNumber) {
		String accountTypeCode = fullAccountNumber.substring(0, 3);
		String branchCode = fullAccountNumber.substring(3, 6);
		String accountNumber = fullAccountNumber.substring(6);

		return accountJpaRepositoryCustomImpl.getAccountBalance(
				accountTypeCode,
				branchCode,
				accountNumber
		);
	}

	public AccountOwnerNameResponse getAccountOwnerNameByFullAccountNumber(
			String accountTypeCode,
			String branchCode,
			String accountNumber
	) {
		return accountJpaRepositoryCustomImpl.getAccountOwnerName(
				accountTypeCode,
				branchCode,
				accountNumber
		);
	}

	public boolean existByFullAccountNumber(
			String accountTypeCode,
			String branchCode,
			String accountNumber) {
		return accountJpaRepository.existsByAccountTypeCodeAndBranchCodeAndAccountNumber(
				accountTypeCode,
				branchCode,
				accountNumber
		);
	}

	public AccountPasswordResponse getAccountPasswordByFullAccountNumber(
			String accountTypeCode,
			String branchCode,
			String accountNumber) {
		return accountJpaRepositoryCustomImpl.getAccountPassword(
				accountTypeCode,
				branchCode,
				accountNumber);
	}
}
