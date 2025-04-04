package bank.donghang.core.account.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.account.application.AccountService;
import bank.donghang.core.account.dto.request.AccountOwnerNameRequest;
import bank.donghang.core.account.dto.request.AccountPasswordRequest;
import bank.donghang.core.account.dto.request.BalanceRequest;
import bank.donghang.core.account.dto.request.DeleteAccountRequest;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.request.MyAccountsRequest;
import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.account.dto.response.AccountSummaryResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.account.dto.response.InstallmentPaymentProcessingResult;
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

	@PostMapping("/check")
	public ResponseEntity<Void> checkAccountPassword(@RequestBody AccountPasswordRequest request) {
		accountService.checkAccountPassword(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/installments/payments/due")
	public ResponseEntity<InstallmentPaymentProcessingResult> processInstallmentPayment() {
		return ResponseEntity.ok(accountService.handleInstallmentAccountSchedule(null));
	}
}
