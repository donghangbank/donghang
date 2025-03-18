package bank.donghang.core.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	INVALID_REQUEST(1_000, "유효하지 않은 요청입니다."),

	MEMBER_NOT_FOUND(2_000, "존재하지 않는 유저입니다."),

	CARD_COMPANY_NOT_FOUND(3_000, "존재하지 않는 카드 회사입니다."),

	CARD_PRODUCT_NOT_FOUND(4_000, "존재하지 않는 카드 상품입니다"),

	BANK_NOT_FOUND(5_000, "존재하지 않는 은행입니다."),

	LOAN_PRODUCT_NOT_FOUND(6_000, "존재하지 않는 대출 상품입니다."),

	PRODUCT_NOT_FOUND(7_000, "존재하지 않는 상품입니다."),
	PRODUCT_TYPE_NOT_FOUND(7_001, "존재하지 않는 상품 타입입니다."),

	ACCOUNT_NOT_FOUND(8_000, "존재하지 않는 계좌입니다."),
	NOT_ENOUGH_BALANCE(8_001, "잔액이 부족합니다."),
	WRONG_AMOUNT_INPUT(8_002, "잘못된 입금 수량입니다.");

	private final int code;
	private final String message;
}
