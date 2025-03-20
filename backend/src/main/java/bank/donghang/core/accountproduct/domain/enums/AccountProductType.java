package bank.donghang.core.accountproduct.domain.enums;

import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum AccountProductType {
	DEMAND(1, "수시입출금 계좌"),
	DEPOSIT(2, "예금 계좌"),
	INSTALLMENT(3, "적금 계좌");

	private final int code;
	private final String description;

	AccountProductType(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public static AccountProductType fromCode(int code) {
		for (AccountProductType type : values()) {
			if (type.code == code) {
				return type;
			}
		}
		throw new BadRequestException(ErrorCode.ACCOUNT_PRODUCT_TYPE_NOT_FOUND);
	}
}
