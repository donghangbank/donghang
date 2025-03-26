package bank.donghang.core.member.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.member.domain.Member;
import bank.donghang.core.member.domain.enums.MemberStatus;
import bank.donghang.core.member.domain.repository.MemberRepository;
import bank.donghang.core.member.dto.request.MemberDetailRequest;
import bank.donghang.core.member.dto.response.MemberDetailResponse;

@ActiveProfiles("test")
@TestPropertySource(locations = "file:${user.dir}/test.env")
@SpringBootTest
class MemberMaskingServiceTest {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

	private Member savedMember;

	@BeforeEach
	void setUp() {
		memberRepository.deleteAll();

		Member member = Member.of(
			"홍길동",
			"hong@example.com",
			"010-1234-5678",
			LocalDateTime.of(1990, 5, 20, 0, 0),
			"서울시 강남구",
			"12345",
			MemberStatus.ACTIVE
		);

		savedMember = memberRepository.save(member);
	}

	@Test
	void can_find_member_by_without_masking() {
		MemberDetailRequest request = new MemberDetailRequest(
			savedMember.getId(), true
		);
		MemberDetailResponse response = memberService.findMember(request);

		assertThat(response.name()).isEqualTo("홍길동");
		assertThat(response.email()).isEqualTo("hong@example.com");
		assertThat(response.phoneNumber()).isEqualTo("010-1234-5678");
	}

	@Test
	void can_find_member_by_with_masking() {
		MemberDetailRequest request = new MemberDetailRequest(savedMember.getId(), false);
		MemberDetailResponse response = memberService.findMember(request);

		assertThat(response.name()).isEqualTo("홍*동");
	}

	@Test
	void can_not_find_member_by_non_existent_id() {
		MemberDetailRequest request = new MemberDetailRequest(999L, false);

		assertThrows(BadRequestException.class, () -> {
			memberService.findMember(request);
		});
	}
}
