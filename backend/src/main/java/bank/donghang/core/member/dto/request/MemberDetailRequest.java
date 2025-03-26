package bank.donghang.core.member.dto.request;

import bank.donghang.core.common.dto.MaskingDto;

public record MemberDetailRequest(
	Long memberId,
	boolean disableMasking
) implements MaskingDto {
	@Override
	public boolean getDisableMasking() {
		return disableMasking;
	}
}
