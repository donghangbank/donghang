package bank.donghang.core.account.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bank.donghang.core.common.dto.MaskingDto;

public record AccountOwnerNameRequest(
		String accountNumber
) implements MaskingDto {
	@Override
	@JsonIgnore
	public boolean getDisableMasking() {
		return false;
	}
}
