package bank.donghang.core.account.dto.request;

import java.time.LocalDate;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.common.dto.MaskingDto;

public record InstallmentAccountRegisterRequest(
	Long memberId,
	Long accountProductId,
	String password,
	String withdrawalAccountNumber,
	String payoutAccountNumber,
	Long monthlyInstallmentAmount,
	Integer monthlyInstallmentDay,
	boolean disableMasking
) implements MaskingDto {
	public Account toEntity(
		String generatedAccountNumber,
		Double interestRate,
		Long withdrawalAccountId,
		Long payoutAccountId,
		LocalDate expiryDate
	) {
		return Account.builder()
			.memberId(memberId)
			.accountProductId(accountProductId)
			.password(password)
			.accountTypeCode("300")
			.branchCode("001")
			.accountNumber(generatedAccountNumber)
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(0L)
			.singleTransferLimit(0L)
			.accountBalance(0L)
			.monthlyInstallmentAmount(monthlyInstallmentAmount)
			.monthlyInstallmentDay(monthlyInstallmentDay)
			.interestRate(interestRate)
			.withdrawalAccountId(withdrawalAccountId)
			.maturityPayoutAccountId(payoutAccountId)
			.accountExpiryDate(expiryDate)
			.build();
	}

	@Override
	public boolean getDisableMasking() {
		return disableMasking;
	}
}
