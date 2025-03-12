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
