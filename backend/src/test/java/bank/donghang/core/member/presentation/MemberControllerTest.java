package bank.donghang.core.member.presentation;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.member.application.MemberService;
import bank.donghang.core.member.domain.Member;
import bank.donghang.core.member.domain.enums.MemberStatus;
import bank.donghang.core.member.dto.request.MemberDetailRequest;
import bank.donghang.core.member.dto.response.MemberDetailResponse;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends ControllerTest {

	@Autowired
	ObjectMapper objectMapper;
	@MockitoBean
	private MemberService memberService;

	@Test
	@DisplayName("사용자의 프로필 정보를 조회한다.")
	public void find_member_info() throws Exception {
		Long memberId = 1L;
		Member member = Member.of(
			"홍길동",
			"hong@example.com",
			"010-1234-5678",
			LocalDateTime.of(1990, 5, 20, 0, 0),
			"서울시 강남구",
			"12345",
			MemberStatus.ACTIVE
		);

		var request = new MemberDetailRequest(
			memberId,
			false
		);

		MemberDetailResponse expect = MemberDetailResponse.from(member);

		when(memberService.findMember(request))
			.thenReturn(expect);

		MvcResult result = mockMvc.perform(post("/api/v1/members")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		var response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			MemberDetailResponse.class
		);

		Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expect);
	}
}
