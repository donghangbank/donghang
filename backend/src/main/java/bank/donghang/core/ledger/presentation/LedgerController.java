package bank.donghang.core.ledger.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.ledger.application.LedgerService;
import bank.donghang.core.ledger.dto.response.DailyReconciliationReport;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ledgers")
@RequiredArgsConstructor
public class LedgerController {

	private final LedgerService ledgerService;

	@GetMapping("/daily-reconciliation")
	public ResponseEntity<DailyReconciliationReport> proceedDailyReconciliation() {
		DailyReconciliationReport dailyReconciliationReport = ledgerService.getDailyReconciliationReport();

		return ResponseEntity.ok(dailyReconciliationReport);
	}
}
