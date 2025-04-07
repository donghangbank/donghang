package bank.donghang.core.ledger.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bank.donghang.core.ledger.dto.ErrorDetail;
import bank.donghang.core.ledger.dto.ValidationResult;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;

public record DailyReconciliationReport(
	LocalDateTime reportTime,
	LocalDateTime requestTime,
	Integer totalEntries,
	Integer successfulEntries,
	Integer failedEntries,
	Integer errorCount,
	Long dailyTotalDebit,
	Long dailyTotalCredit,
	Long dailyTotal,
	Long discrepancyAmount,
	List<ErrorDetail> errorDetails
) {
	public static DailyReconciliationReport from(
		LocalDateTime reportTime,
		LocalDateTime requestTime,
		List<DailyReconciliationQuery> queries,
		ValidationResult result
	) {
		Map<Long, List<DailyReconciliationQuery>> entriesByJournal = queries.stream()
			.collect(Collectors.groupingBy(DailyReconciliationQuery::journalEntryId));
		int uniqueJournalEntries = entriesByJournal.size();

		return new DailyReconciliationReport(
			reportTime,
			requestTime,
			uniqueJournalEntries,
			result.successfulEntries(),
			result.failedEntries(),
			result.errors().size(),
			result.totalDebit(),
			result.totalCredit(),
			result.totalDebit() + result.totalCredit(),
			Math.abs(result.totalDebit() - result.totalCredit()),
			result.errors()
		);
	}
}
