package bank.donghang.core.member.application;

import org.springframework.stereotype.Service;

import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import bank.donghang.core.member.domain.Member;
import bank.donghang.core.member.domain.repository.MemberRepository;
import bank.donghang.core.member.dto.response.MemberDetailResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberDetailResponse findMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BadRequestException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberDetailResponse.from(member);
	}
}
