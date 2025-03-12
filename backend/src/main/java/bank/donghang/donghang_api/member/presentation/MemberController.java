package bank.donghang.donghang_api.member.presentation;

import bank.donghang.donghang_api.member.application.MemberService;
import bank.donghang.donghang_api.member.dto.response.MemberDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponse> getUserInformation(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.findMember(memberId));
    }
}
