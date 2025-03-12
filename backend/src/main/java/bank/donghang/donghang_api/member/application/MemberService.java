package bank.donghang.donghang_api.member.application;

import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import bank.donghang.donghang_api.member.domain.Member;
import bank.donghang.donghang_api.member.domain.repository.MemberRepository;
import bank.donghang.donghang_api.member.dto.response.MemberDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository userRepository;

    public MemberDetailResponse findMember(Long userId) {
        Member user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        return MemberDetailResponse.from(user);
    }
}
