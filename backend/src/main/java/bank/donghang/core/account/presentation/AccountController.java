package bank.donghang.core.account.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.account.application.AccountService;
import bank.donghang.core.account.dto.request.AccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

	private final AccountService accountService;

	@PostMapping()
	public ResponseEntity<AccountRegisterResponse> registerAccount(
		@RequestBody @Valid AccountRegisterRequest accountRegisterRequest) {
		return ResponseEntity.ok(accountService.createAccount(accountRegisterRequest));
	}
}
