package bank.donghang.core.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	INVALID_REQUEST(1_000, "유효하지 않은 요청입니다."),
	DUPLICATE_REQUEST(1_001, "중복된 요청입니다."),

	MEMBER_NOT_FOUND(2_000, "존재하지 않는 유저입니다."),

	CARD_COMPANY_NOT_FOUND(3_000, "존재하지 않는 카드 회사입니다."),

	CARD_PRODUCT_NOT_FOUND(4_000, "존재하지 않는 카드 상품입니다"),

	BANK_NOT_FOUND(5_000, "존재하지 않는 은행입니다."),

	LOAN_PRODUCT_NOT_FOUND(6_000, "존재하지 않는 대출 상품입니다."),

	ACCOUNT_PRODUCT_NOT_FOUND(7_000, "존재하지 않는 상품입니다."),
	ACCOUNT_PRODUCT_TYPE_NOT_FOUND(7_001, "존재하지 않는 상품 타입입니다."),
	WRONG_ACCOUNT_PRODUCT_TYPE(7_002, "잘못된 상품 타입입니다."),

	ACCOUNT_NOT_FOUND(8_000, "존재하지 않는 계좌입니다."),
	NOT_ENOUGH_BALANCE(8_001, "잔액이 부족합니다."),
	WRONG_AMOUNT_INPUT(8_002, "잘못된 입금 수량입니다."),
	WITHDRAWAL_ACCOUNT_NOT_OWNED(8_003, "출금 계좌가 요청 회원의 계좌가 아닙니다."),
	MATURITY_PAYOUT_ACCOUNT_NOT_OWNED(8_004, "만기 지급 계좌가 요청 회원의 계좌가 아닙니다."),
	WITHDRAWAL_ACCOUNT_HAS_NOT_ENOUGH_BALANCE(8_005, "출금 계좌의 잔액이 부족합니다."),
	WITHDRAWAL_ACCOUNT_IS_NOT_ELIGIBLE_FOR_WITHDRAWAL(8_006, "출금이 허용되지 않는 계좌입니다."),
	MATURITY_ACCOUNT_IS_NOT_ELIGIBLE_FOR_PAYOUT(8_007, "만기 지급을 받을 수 없는 계좌입니다."),
	WITHDRAWAL_ACCOUNT_IS_NOT_ACTIVE(8_008, "예금 계좌가 활성화 상태가 아닙니다."),
	MATURITY_ACCOUNT_IS_NOT_ACTIVE(8_009, "만기 환급 계좌가 활성화 상태가 아닙니다."),
	PASSWORD_MISMATCH(8_010, "비밀번호가 일치하지 않습니다."),

	CARD_NOT_FOUND(9_000, "존재하지 않는 카드입니다.");

	private final int code;
	private final String message;
}
