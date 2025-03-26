package bank.donghang.core.account.domain.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.InstallmentSchedule;
import bank.donghang.core.account.domain.enums.InstallmentStatus;
import bank.donghang.core.account.dto.response.BalanceResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountRepository {
	private final AccountJpaRepository accountJpaRepository;
	private final InstallmentScheduleJpaRepository installmentScheduleJpaRepository;
	private final AccountJpaRepositoryCustomImpl accountJpaRepositoryCustomImpl;

	@Transactional
	public Account saveAccount(Account account) {
		return accountJpaRepository.save(account);
	}

	// 적금 게좌 저장 + 적금 납입 스케줄 저장
	@Transactional
	public Account saveInstallmentAccount(Account account) {
		Account savedAccount = saveAccount(account);
		LocalDate nextInstallmentDate = LocalDate.now()
			.plusMonths(1)
			.withDayOfMonth(
				Math.min(account.getMonthlyInstallmentDay(),
					YearMonth
						.from(
							LocalDate
								.now()
								.plusMonths(1))
						.lengthOfMonth()));

		InstallmentSchedule installmentSchedule =
			InstallmentSchedule
				.builder().installmentAccountId(savedAccount.getAccountId())
				.withdrawalAccountId(savedAccount.getWithdrawalAccountId())
				.installmentAmount(savedAccount.getMonthlyInstallmentAmount())
				.installmentSequence(1)
				.installmentStatus(InstallmentStatus.SCHEDULED)
				.installmentScheduledDate(nextInstallmentDate)
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
}
