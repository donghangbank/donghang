package bank.donghang.donghang_api.member.presentation;

import bank.donghang.donghang_api.common.controller.ControllerTest;
import bank.donghang.donghang_api.member.application.MemberService;
import bank.donghang.donghang_api.member.domain.Member;
import bank.donghang.donghang_api.member.domain.enums.MemberStatus;
import bank.donghang.donghang_api.member.dto.response.MemberDetailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest extends ControllerTest {

    @MockitoBean
    private MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

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

        MemberDetailResponse expect = MemberDetailResponse.from(member);

        when(memberService.findMember(memberId))
                .thenReturn(expect);

        MvcResult result = mockMvc.perform(get("/api/v1/members/{memberId}", memberId)
                        .contentType(MediaType.APPLICATION_JSON))
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