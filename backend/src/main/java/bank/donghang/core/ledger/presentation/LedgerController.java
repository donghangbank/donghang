package bank.donghang.core.ledger.presentation;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.ledger.application.LedgerFacade;
import bank.donghang.core.ledger.dto.query.TransactionSumDay;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ledgers")
public class LedgerController {

	private final LedgerFacade ledgerFacade;

	@GetMapping("/transactions")
	public ResponseEntity<TransactionSumDay> checkOneDayTransactionLedger() {
		LocalDateTime start = LocalDateTime.now().minusDays(1);
		LocalDateTime end = LocalDateTime.now();
		TransactionSumDay transactionSumDay = ledgerFacade.checkOneDayTransactionLedger(
			start,
			end
		);
		return ResponseEntity.ok(transactionSumDay);
	}
}
