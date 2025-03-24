package bank.donghang.core.account.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.account.application.AccountService;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

	private final AccountService accountService;

	@PostMapping("/demands")
	public ResponseEntity<AccountRegisterResponse> registerDemandAccount(
		@RequestBody @Valid DemandAccountRegisterRequest demandAccountRegisterRequest) {
		return ResponseEntity.ok(accountService.createDemandAccount(demandAccountRegisterRequest));
	}

	@PostMapping("/deposits")
	public ResponseEntity<AccountRegisterResponse> registerDepositAccount(
		@RequestBody @Valid DepositAccountRegisterRequest depositAccountRegisterRequest) {
		return ResponseEntity.ok(accountService.createDepositAccount(depositAccountRegisterRequest));
	}

	@PostMapping("/installments")
	public ResponseEntity<AccountRegisterResponse> registerInstallmentAccount(
		@RequestBody @Valid InstallmentAccountRegisterRequest installmentAccountRegisterRequest) {
		return ResponseEntity.ok(accountService.createInstallmentAccount(installmentAccountRegisterRequest));
	}
}
