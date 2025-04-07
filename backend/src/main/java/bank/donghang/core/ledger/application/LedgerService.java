package bank.donghang.core.ledger.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.ledger.domain.enums.EntryType;
import bank.donghang.core.ledger.domain.enums.ReconciliationCode;
import bank.donghang.core.ledger.domain.repository.LedgerRepository;
import bank.donghang.core.ledger.dto.ErrorDetail;
import bank.donghang.core.ledger.dto.ValidationResult;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;
import bank.donghang.core.ledger.dto.response.DailyReconciliationReport;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {

	private final LedgerRepository ledgerRepository;

	public DailyReconciliationReport getDailyReconciliationReport() {
		LocalDateTime requestTime = LocalDateTime.now();
		LocalDateTime reportTime = requestTime.minusDays(1);

		List<DailyReconciliationQuery> queries = ledgerRepository.getDailyReconciliationInfo(
			reportTime,
			requestTime
		);

		ValidationResult validationResult = validateTransactions(queries);

		return DailyReconciliationReport.from(
			reportTime,
			requestTime,
			queries,
			validationResult
		);
	}

	private ValidationResult validateTransactions(List<DailyReconciliationQuery> queries) {
		List<ErrorDetail> errors = new ArrayList<>();
		long totalDebit = 0;
		long totalCredit = 0;
		int successfulEntries = 0;
		int failedEntries = 0;

		Map<Long, List<DailyReconciliationQuery>> entriesByJournal = queries.stream()
			.collect(Collectors.groupingBy(DailyReconciliationQuery::journalEntryId));

		for (List<DailyReconciliationQuery> entryLines : entriesByJournal.values()) {
			if (!validateJournalEntry(entryLines, errors)) {
				failedEntries += entryLines.size();
				continue;
			}
			successfulEntries += entryLines.size();

			for (DailyReconciliationQuery line : entryLines) {
				if (line.entryType() == EntryType.DEBIT) {
					totalDebit += line.amount();
				} else {
					totalCredit += line.amount();
				}
			}
		}

		if (totalDebit != totalCredit) {
			errors.add(ErrorDetail.forBalanceMismatch(totalDebit, totalCredit));
		}

		return new ValidationResult(
			totalDebit,
			totalCredit,
			successfulEntries,
			failedEntries,
			errors
		);
	}

	private boolean validateJournalEntry(List<DailyReconciliationQuery> entryLines, List<ErrorDetail> errors) {
		if (entryLines.isEmpty())
			return false;

		DailyReconciliationQuery firstLine = entryLines.get(0);
		boolean isValid = true;

		// 트랜잭션 상태 검증
		if (firstLine.transactionStatus() != TransactionStatus.COMPLETED) {
			errors.add(new ErrorDetail(
				firstLine.transactionId(),
				null,
				ReconciliationCode.TRANSACTION_NOT_COMPLETED
			));
			isValid = false;
		}

		// 입출금 거래인 경우 쌍으로 검증 (2개의 라인 필요)
		if (firstLine.transactionType() == TransactionType.DEPOSIT ||
			firstLine.transactionType() == TransactionType.WITHDRAWAL) {

			if (entryLines.size() != 2) {
				errors.add(new ErrorDetail(
					firstLine.transactionId(),
					null,
					ReconciliationCode.INVALID_ENTRY_COUNT
				));
				return false;
			}

			long debitSum = entryLines.stream()
				.filter(line -> line.entryType() == EntryType.DEBIT)
				.mapToLong(DailyReconciliationQuery::amount)
				.sum();

			long creditSum = entryLines.stream()
				.filter(line -> line.entryType() == EntryType.CREDIT)
				.mapToLong(DailyReconciliationQuery::amount)
				.sum();

			if (debitSum != creditSum) {
				errors.add(new ErrorDetail(
					firstLine.transactionId(),
					null,
					ReconciliationCode.AMOUNT_MISMATCH
				));
				isValid = false;
			}
		}
		// 이체 거래는 1개의 라인만 있어도 유효 (DEBIT 또는 CREDIT)
		else if (firstLine.transactionType() == TransactionType.TRANSFER) {
			if (entryLines.size() != 1) {
				errors.add(new ErrorDetail(
					firstLine.transactionId(),
					null,
					ReconciliationCode.INVALID_ENTRY_COUNT
				));
				return false;
			}

			for (DailyReconciliationQuery line : entryLines) {
				if (!isEntryTypeConsistent(line.transactionType(), line.entryType())) {
					errors.add(new ErrorDetail(
						line.transactionId(),
						line.accountId(),
						ReconciliationCode.ENTRY_TYPE_MISMATCH
					));
					isValid = false;
				}
			}
		}

		return isValid;
	}

	private boolean isEntryTypeConsistent(TransactionType transactionType, EntryType entryType) {
		return switch (transactionType) {
			case DEPOSIT, WITHDRAWAL -> true;
			case TRANSFER -> entryType == EntryType.DEBIT || entryType == EntryType.CREDIT;
			default -> false;
		};
	}
}