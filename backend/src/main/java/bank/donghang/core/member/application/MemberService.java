package bank.donghang.core.member.application;

import org.springframework.stereotype.Service;

import bank.donghang.core.common.annotation.MaskApply;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import bank.donghang.core.member.domain.Member;
import bank.donghang.core.member.domain.repository.MemberRepository;
import bank.donghang.core.member.dto.request.MemberDetailRequest;
import bank.donghang.core.member.dto.response.MemberDetailResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@MaskApply(typeValue = MemberDetailResponse.class)
	public MemberDetailResponse findMember(MemberDetailRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberDetailResponse.from(member);
	}
}
