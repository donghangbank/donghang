package bank.donghang.core.account.domain;

import java.time.LocalDate;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account", uniqueConstraints = {
	@UniqueConstraint(
		name = "uk_account_type_branch_number",
		columnNames = {
			"account_type_code",
			"branch_code",
			"account_number"
		})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "account_id")
	private Long accountId;

	@Column(nullable = false, name = "member_id")
	private Long memberId;

	@Column(nullable = false, name = "account_product_id")
	private Long accountProductId;

	@Column(name = "withdrawal_account_id")
	private Long withdrawalAccountId;

	@Column(name = "maturity_payout_account_id")
	private Long maturityPayoutAccountId;

	@Column(nullable = false, name = "account_type_code", length = 3)
	private String accountTypeCode;

	@Column(nullable = false, name = "branch_code", length = 3)
	private String branchCode;

	@Column(nullable = false, name = "account_number", length = 8)
	private String accountNumber;

	@Column(nullable = false, name = "password", length = 4)
	private String password;

	@Column(nullable = false, name = "account_status")
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;

	@Column(nullable = false, name = "daily_transfer_limit")
	private Long dailyTransferLimit;

	@Column(nullable = false, name = "single_transfer_limit")
	private Long singleTransferLimit;

	@Column(nullable = false, name = "account_balance")
	private Long accountBalance;

	@Column(name = "monthly_installment_amount")
	private Long monthlyInstallmentAmount;

	@Column(name = "monthly_installment_day")
	private Integer monthlyInstallmentDay;

	@Column(nullable = false, name = "interest_rate")
	private Double interestRate;

	@Column(name = "account_expiry_date")
	private LocalDate accountExpiryDate;

	public void deleteAccount() {
		this.accountStatus = AccountStatus.INACTIVE;
	}

	public void deposit(Long amount) {
		if (amount < 0) {
			throw new BadRequestException(ErrorCode.WRONG_AMOUNT_INPUT);
		}

		setBalance(amount, TransactionType.DEPOSIT);
	}

	public void withdraw(Long amount) {
		if (amount < 0) {
			throw new BadRequestException(ErrorCode.WRONG_AMOUNT_INPUT);
		}

		setBalance(amount, TransactionType.WITHDRAWAL);
	}

	private void setBalance(Long balance, TransactionType type) {
		if (type == TransactionType.WITHDRAWAL) {
			this.accountBalance = this.accountBalance - balance;
		}

		if (type == TransactionType.DEPOSIT) {
			this.accountBalance = this.accountBalance + balance;
		}
	}

	private boolean isOwner(Long memberId) {
		return this.memberId.equals(memberId);
	}

	private boolean isActive() {
		return this.accountStatus == AccountStatus.ACTIVE;
	}

	private boolean isDemandAccount() {
		return this.accountTypeCode.equals("100");
	}

	private boolean isBalanceEnoughForDeposit(Long initialDepositAmount) {
		return this.accountBalance >= initialDepositAmount;
	}

	public void verifyWithdrawalAccount(Long memeberId, Long initDepositAmount) {
		if (!isOwner(memeberId)) {
			throw new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_NOT_OWNED);
		}

		if (!isActive()) {
			throw new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_IS_NOT_ACTIVE);
		}

		if (!isBalanceEnoughForDeposit(initDepositAmount)) {
			throw new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_HAS_NOT_ENOUGH_BALANCE);
		}

		if (!isDemandAccount()) {
			throw new BadRequestException(ErrorCode.WITHDRAWAL_ACCOUNT_IS_NOT_ELIGIBLE_FOR_WITHDRAWAL);
		}
	}

	public void verifyPayoutAccount(Long memeberId) {
		if (!isOwner(memeberId)) {
			throw new BadRequestException(ErrorCode.MATURITY_PAYOUT_ACCOUNT_NOT_OWNED);
		}

		if (!isActive()) {
			throw new BadRequestException(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ACTIVE);
		}

		if (!isDemandAccount()) {
			throw new BadRequestException(ErrorCode.MATURITY_ACCOUNT_IS_NOT_ELIGIBLE_FOR_PAYOUT);
		}
	}

}
