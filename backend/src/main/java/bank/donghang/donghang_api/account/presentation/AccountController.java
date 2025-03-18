package bank.donghang.donghang_api.account.presentation;

import bank.donghang.donghang_api.account.application.AccountService;
import bank.donghang.donghang_api.account.dto.request.AccountRegisterRequest;
import bank.donghang.donghang_api.account.dto.response.AccountRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
