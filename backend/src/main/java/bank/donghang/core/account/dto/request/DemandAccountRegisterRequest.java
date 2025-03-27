package bank.donghang.core.account.dto.request;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.common.dto.MaskingDto;

public record DemandAccountRegisterRequest(
	Long memberId,
	Long accountProductId,
	String password,
	boolean disableMasking
) implements MaskingDto {
	// todo. password encoding
	// todo. account type handling logic
	public Account toEntity(String generatedAccountNumber, Double interestRate) {
		return Account.builder()
			.memberId(memberId)
			.accountProductId(accountProductId)
			.password(password)
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber(generatedAccountNumber)
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100_000_000L)
			.singleTransferLimit(10_000_000L)
			.accountBalance(0L)
			.interestRate(interestRate)
			.build();
	}

	@Override
	public boolean getDisableMasking() {
		return disableMasking;
	}
}
