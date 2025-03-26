package bank.donghang.core.member.dto.response;

import bank.donghang.core.common.annotation.Mask;
import bank.donghang.core.common.enums.MaskingType;
import bank.donghang.core.member.domain.Member;
import bank.donghang.core.member.domain.enums.MemberStatus;

public record MemberDetailResponse(
	Long id,
	@Mask(type = MaskingType.NAME)
	String name,
	String email,
	String phoneNumber,
	MemberStatus memberStatus
) {
	public static MemberDetailResponse from(Member member) {
		return new MemberDetailResponse(
			member.getId(),
			member.getName(),
			member.getEmail(),
			member.getPhoneNumber(),
			member.getMemberStatus()
		);
	}
}
