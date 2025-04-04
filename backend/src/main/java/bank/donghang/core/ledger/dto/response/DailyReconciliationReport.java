package bank.donghang.core.ledger.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DailyReconciliationReport(
	LocalDateTime reportTime,
	LocalDateTime requestTime,
	Integer totalEntries,
	Integer errorCount,
	Long dailyTotalDebit,
	Long dailyTotalCredit,
	Long dailyTotal,
	Long discrepancyAmount,
	List<ErrorDetail> errorDetails
) {
}
