package bank.donghang.core.ledger.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReconciliationCode {
	TRANSACTION_NOT_COMPLETED("TR-001", "거래가 완료 상태가 아닙니다"),
	TRANSACTION_ALREADY_REVERSED("TR-002", "이미 역처리된 거래입니다"),

	DEBIT_CREDIT_MISMATCH("DC-001", "차변과 대변 총액이 일치하지 않습니다"),
	ENTRY_TYPE_MISMATCH("DC-002", "거래 유형과 분개 유형이 일치하지 않습니다"),
	AMOUNT_MISMATCH("DC-003", "입출금 거래의 차변/대변 금액이 일치하지 않습니다"),

	ACCOUNT_NOT_FOUND("AC-001", "계좌을 찾을 수 없습니다"),
	ACCOUNT_INACTIVE("AC-002", "비활성화된 계정입니다"),
	ACCOUNT_MISMATCH("AC-003", "계좌가 일치하지 않습니다."),

	INVALID_ENTRY_COUNT("EN-001", "유효하지 않은 분개입니다.");

	private final String code;
	private final String message;
}
