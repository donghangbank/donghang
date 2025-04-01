package bank.donghang.core.account.presentation;

import bank.donghang.core.account.dto.request.*;
import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import bank.donghang.core.account.application.AccountService;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.account.dto.response.AccountSummaryResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.common.dto.PageInfo;
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

	@PostMapping("/balance")
	public ResponseEntity<BalanceResponse> getAccountBalance(@RequestBody BalanceRequest request) {
		return ResponseEntity.ok(accountService.getAccountBalance(request));
	}

	@PostMapping("/me")
	public ResponseEntity<PageInfo<AccountSummaryResponse>> getMyAccounts(
		@RequestBody MyAccountsRequest request,
		@RequestParam(required = false) String pageToken
	) {
		return ResponseEntity.ok(accountService.getMyAccounts(request, pageToken));
	}

	@PatchMapping
	public ResponseEntity<Void> deleteAccount(@RequestBody DeleteAccountRequest request) {
		accountService.deleteAccount(request);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/owner")
	public ResponseEntity<AccountOwnerNameResponse> getAccountOwnerName(@RequestBody AccountOwnerNameRequest request) {
		return ResponseEntity.ok(accountService.getOwnerName(request));
	}
}
