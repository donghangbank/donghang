package bank.donghang.core.loanproduct.domain.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum RepaymentMethod {
	BALLOON_PAYMENT("만기 일시 상환"),
	EQUAL_PRINCIPAL_AND_INTEREST("원리금 균등 상환");

	private String key;
}
