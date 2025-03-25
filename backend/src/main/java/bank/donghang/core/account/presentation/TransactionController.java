package bank.donghang.core.account.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.account.application.TransactionService;
import bank.donghang.core.account.dto.request.DepositRequest;
import bank.donghang.core.account.dto.request.TransactionHistoryRequest;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.request.WithdrawalRequest;
import bank.donghang.core.account.dto.response.DepositResponse;
import bank.donghang.core.account.dto.response.TransactionHistoryResponse;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.account.dto.response.WithdrawalResponse;
import bank.donghang.core.common.dto.PageInfo;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping
	public ResponseEntity<TransactionResponse> transferByAccount(
		@RequestBody TransactionRequest transactionRequest
	) {
		TransactionResponse response = transactionService.transferByAccount(transactionRequest);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/deposit")
	public ResponseEntity<DepositResponse> depositByAccount(
		@RequestBody DepositRequest request
	) {
		DepositResponse response = transactionService.deposit(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/withdrawal")
	public ResponseEntity<WithdrawalResponse> withdrawalByAccount(
		@RequestBody WithdrawalRequest request
	) {
		WithdrawalResponse response = transactionService.withdraw(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/histories")
	public ResponseEntity<PageInfo<TransactionHistoryResponse>> getTransactionHistories(
		@RequestBody TransactionHistoryRequest request,
		@RequestParam(required = false) String pageToken
	) {
		PageInfo<TransactionHistoryResponse> response = transactionService.getTransactionHistories(
			request,
			pageToken
		);

		return ResponseEntity.ok(response);
	}
}
