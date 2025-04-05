package bank.donghang.core.ledger.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

		List<DailyReconciliationQuery> queries = ledgerRepository.getDailyReconciliationQuery(
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

		for (DailyReconciliationQuery query : queries) {
			if (!validateTransaction(query, errors)) {
				failedEntries++;
				continue;
			}
			successfulEntries++;

			if (query.entryType() == EntryType.DEBIT) {
				totalDebit += query.amount();
			} else {
				totalCredit += query.amount();
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

	private boolean validateTransaction(DailyReconciliationQuery query, List<ErrorDetail> errors) {
		boolean isValid = true;

		if (query.transactionStatus() != TransactionStatus.COMPLETED) {
			errors.add(new ErrorDetail(
					query.transactionId(),
					query.accountId(),
					ReconciliationCode.TRANSACTION_NOT_COMPLETED
			));
			isValid = false;
		}

		if (!isEntryTypeConsistent(query.transactionType(), query.entryType())) {
			errors.add(new ErrorDetail(
					query.transactionId(),
					query.accountId(),
					ReconciliationCode.ENTRY_TYPE_MISMATCH
			));
			isValid = false;
		}

		return isValid;
	}

	private boolean isEntryTypeConsistent(TransactionType transactionType, EntryType entryType) {
		return switch (transactionType) {
			case DEPOSIT -> entryType == EntryType.CREDIT;
			case WITHDRAWAL -> entryType == EntryType.DEBIT;
			case TRANSFER -> true;
			default -> false;
		};
	}
}