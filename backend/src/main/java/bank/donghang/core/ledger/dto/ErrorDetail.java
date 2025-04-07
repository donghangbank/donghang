package bank.donghang.core.ledger.dto;

import bank.donghang.core.ledger.domain.enums.ReconciliationCode;

public record ErrorDetail(
	Long transactionId,
	Long accountId,
	ReconciliationCode code,
	String message
) {
	public ErrorDetail(Long transactionId, Long accountId, ReconciliationCode code) {
		this(transactionId, accountId, code, code.getMessage());
	}

	public static ErrorDetail forBalanceMismatch(long debit, long credit) {
		return new ErrorDetail(
			null,
			null,
			ReconciliationCode.DEBIT_CREDIT_MISMATCH,
			String.format("차변 총액(%d)과 대변 총액(%d)이 일치하지 않습니다", debit, credit)
		);
	}
}