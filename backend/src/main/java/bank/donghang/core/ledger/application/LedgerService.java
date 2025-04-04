package bank.donghang.core.ledger.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import bank.donghang.core.ledger.domain.repository.LedgerRepository;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;
import bank.donghang.core.ledger.dto.response.DailyReconciliationReport;
import bank.donghang.core.ledger.dto.response.ErrorDetail;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {

	private final LedgerRepository ledgerRepository;

	public DailyReconciliationReport getDailyReconciliationReport() {
		LocalDateTime requestTime = LocalDateTime.now();

		LocalDateTime start = requestTime;
		LocalDateTime end = requestTime.minusDays(1);

		List<DailyReconciliationQuery> reconciliationQueries = ledgerRepository.getDailyReconciliationQuery(
			start,
			end
		);

		List<ErrorDetail> errors = new ArrayList<>();

	}
}
