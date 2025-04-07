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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LedgerService {

	//TODO: 계좌 검증 로직 추가, 은행 계좌(자산, 현금)들 보고서 작성

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
			boolean isValid = validateJournalEntry(entryLines, errors);

			// 무조건 총합에는 포함
			for (DailyReconciliationQuery line : entryLines) {
				if (line.entryType() == EntryType.DEBIT) {
					totalDebit += line.amount();
				} else {
					totalCredit += line.amount();
				}
			}

			if (isValid) {
				successfulEntries++;
			} else {
				failedEntries++;
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

		if (firstLine.transactionStatus() != TransactionStatus.COMPLETED) {
			errors.add(new ErrorDetail(
				firstLine.transactionId(),
				null,
				ReconciliationCode.TRANSACTION_NOT_COMPLETED
			));
			isValid = false;
		}

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
					firstLine.accountId(),
					ReconciliationCode.AMOUNT_MISMATCH
				));
				isValid = false;
			}
		} else if (firstLine.transactionType() == TransactionType.TRANSFER) {
			if (entryLines.size() != 2) {
				errors.add(new ErrorDetail(
					firstLine.transactionId(),
					firstLine.accountId(),
					ReconciliationCode.INVALID_ENTRY_COUNT
				));
				return false;
			}

			long debitCount = entryLines.stream()
				.filter(line -> line.entryType() == EntryType.DEBIT)
				.count();
			long creditCount = entryLines.stream()
				.filter(line -> line.entryType() == EntryType.CREDIT)
				.count();

			if (debitCount != 1 || creditCount != 1) {
				errors.add(new ErrorDetail(
					firstLine.transactionId(),
					firstLine.accountId(),
					ReconciliationCode.ENTRY_TYPE_MISMATCH
				));
				isValid = false;
			}

			long debitSum = entryLines.stream()
				.filter(line -> line.entryType() == EntryType.DEBIT)
				.mapToLong(DailyReconciliationQuery::amount)
				.sum();
			long creditSum = entryLines.stream()
				.filter(line -> line.entryType() == EntryType.CREDIT)
				.mapToLong(DailyReconciliationQuery::amount)
				.sum();

			log.info("accountId {}", firstLine.accountId());
			if (debitSum != creditSum) {
				errors.add(new ErrorDetail(
					firstLine.transactionId(),
					firstLine.accountId(),
					ReconciliationCode.AMOUNT_MISMATCH
				));
				isValid = false;
			}
		}

		return isValid;
	}
}