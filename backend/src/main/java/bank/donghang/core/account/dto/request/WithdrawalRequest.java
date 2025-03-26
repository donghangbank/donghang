package bank.donghang.core.account.dto.request;

import java.time.LocalDateTime;

import bank.donghang.core.common.dto.MaskingDto;

public record WithdrawalRequest(
	String accountNumber,
	Long amount,
	LocalDateTime sessionStartTime,
	boolean disableMasking
) implements MaskingDto {
	@Override
	public boolean getDisableMasking() {
		return disableMasking;
	}
}