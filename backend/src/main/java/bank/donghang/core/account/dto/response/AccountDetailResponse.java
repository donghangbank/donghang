package bank.donghang.core.account.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;

public record AccountDetailResponse(
		String bankName,
		@Mask(type = MaskingType.ACCOUNT_NUMBER)
		String accountNumber,
		AccountProductType accountProductType,
		@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
		LocalDate openingDate,
		Long accountBalance,
		Double interestRate,
		AccountStatus accountStatus,
		@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
		LocalDate accountExpiryDate,
		@Mask(type = MaskingType.ACCOUNT_NUMBER)
		String withdrawalAccountNumber,
		Long monthlyInstallmentAmount,
		Integer monthlyInstallmentDate
) {
}
