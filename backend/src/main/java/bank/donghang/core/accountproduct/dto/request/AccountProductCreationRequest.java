package bank.donghang.core.accountproduct.dto.request;

import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccountProductCreationRequest(
		@NotBlank(message = "상품명을 입력해주세요.")
		String accountProductName,

		@NotBlank(message = "상품 설명을 입력해주세요.")
		String accountProductDescription,

		@NotNull(message = "은행 ID를 입력해주세요.")
		Long bankId,

		@NotNull(message = "이자율을 입력해주세요.")
		@DecimalMin(value = "0.0", inclusive = false, message = "이자율은 0보다 커야 합니다.")
		Double interestRate,

		@NotBlank(message = "금리 설명을 입력해주세요.")
		String rateDescription,

		@NotNull(message = "상품 타입 코드를 입력해주세요.")
		@Min(value = 0, message = "상품 타입 코드는 0 이상의 정수여야 합니다.")
		Integer accountProductTypeCode,

		@Positive(message = "가입 기간은 양수여야 합니다.")
		Long subscriptionPeriod,

		@Min(value = 0, message = "최소 가입 금액은 0 이상이어야 합니다.")
		Long minSubscriptionBalance,

		@Min(value = 0, message = "최대 가입 금액은 0 이상이어야 합니다.")
		Long maxSubscriptionBalance
) {
	public AccountProduct toEntity() {
		return AccountProduct.builder()
				.accountProductName(accountProductName)
				.accountProductDescription(accountProductDescription)
				.bankId(bankId)
				.interestRate(interestRate)
				.rateDescription(rateDescription)
				.accountProductType(AccountProductType.fromCode(accountProductTypeCode))
				.subscriptionPeriod(subscriptionPeriod)
				.minSubscriptionBalance(minSubscriptionBalance)
				.maxSubscriptionBalance(maxSubscriptionBalance)
				.build();
	}
}
