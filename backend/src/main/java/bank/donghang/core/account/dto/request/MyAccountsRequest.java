package bank.donghang.core.account.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import bank.donghang.core.common.dto.MaskingDto;


public record MyAccountsRequest(
	Long memberId
) implements MaskingDto {
	@Override
	@JsonIgnore
	public boolean getDisableMasking() {
		return false;
	}
}
