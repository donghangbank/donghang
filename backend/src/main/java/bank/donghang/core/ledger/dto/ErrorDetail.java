package bank.donghang.core.ledger.dto;

import bank.donghang.core.ledger.domain.enums.ReconciliationCode;

public record ErrorDetail(
		Long transactionId,
		Long accountId,
		ReconciliationCode errorCode,
		String errorMessage,
		Long expectedAmount,
		Long actualAmount
) {
	public ErrorDetail(
			Long transactionId,
			Long accountId,
			ReconciliationCode errorCode
	) {
		this(
				transactionId,
				accountId,
				errorCode,
				errorCode.getMessage(),
				null,
				null
		);
	}

	public static ErrorDetail forBalanceMismatch(
			Long totalDebit,
			Long totalCredit
	) {
		return new ErrorDetail(
				null,
				null,
				ReconciliationCode.DEBIT_CREDIT_MISMATCH,
				String.format("차변 총액(%d)과 대변 총액(%d)이 일치하지 않습니다", totalDebit, totalCredit),
				totalDebit,
				totalCredit
		);
	}
}