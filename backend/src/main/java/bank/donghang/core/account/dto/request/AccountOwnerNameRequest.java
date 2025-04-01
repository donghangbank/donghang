package bank.donghang.core.account.dto.request;

import bank.donghang.core.common.dto.MaskingDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

public record AccountOwnerNameRequest(
		String accountNumber
) implements MaskingDto {
	@Override
	@JsonIgnore
	public boolean getDisableMasking() {
		return false;
	}
}
