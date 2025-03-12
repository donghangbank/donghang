package bank.donghang.donghang_api.member.dto.response;

import bank.donghang.donghang_api.member.domain.Member;
import bank.donghang.donghang_api.member.domain.enums.MemberStatus;

public record MemberDetailResponse(
        Long id,
        String name,
        String email,
        String phoneNumber,
        MemberStatus memberStatus
) {
    public static MemberDetailResponse from(Member user) {
        return new MemberDetailResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getMemberStatus()
        );
    }
}
