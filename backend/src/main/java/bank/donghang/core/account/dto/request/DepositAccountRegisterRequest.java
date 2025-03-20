package bank.donghang.core.account.dto.request;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.enums.AccountStatus;

public record DepositAccountRegisterRequest(Long memberId, Long accountProductId, String password,
											String withdrawalAccountNumber, String payoutAccountNumber,
											Long initDepositAmount) {
	public Account toEntity(String generatedAccountNumber, Double interestRate, Long withdrawalAccountId,
		Long payoutAccountId, Long initDepositAmount) {
		return Account.builder()
			.memberId(memberId)
			.accountProductId(accountProductId)
			.password(password)
			.accountTypeCode("200")
			.branchCode("001")
			.accountNumber(generatedAccountNumber)
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(0L)
			.singleTransferLimit(0L)
			.accountBalance(initDepositAmount)
			.interestRate(interestRate)
			.withdrawalAccountId(withdrawalAccountId)
			.maturityPayoutAccountId(payoutAccountId)
			.build();
	}
}
