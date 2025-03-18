package bank.donghang.core.account.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.account.application.TransactionService;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.response.TransactionResponse;
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
}
