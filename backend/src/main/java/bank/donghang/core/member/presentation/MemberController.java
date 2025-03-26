package bank.donghang.core.member.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.member.application.MemberService;
import bank.donghang.core.member.dto.request.MemberDetailRequest;
import bank.donghang.core.member.dto.response.MemberDetailResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<MemberDetailResponse> getUserInformation(@RequestBody MemberDetailRequest request) {
		return ResponseEntity.ok(memberService.findMember(request));
	}
}
