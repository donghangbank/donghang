package bank.donghang.core.account.domain;

import java.util.Date;

import bank.donghang.core.account.domain.enums.AccountStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	@UniqueConstraint(name = "uk_account_type_branch_number", columnNames = {"account_type_code", "branch_code",
		"account_number"})})
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
	private String withdrawalAccountId;

	@Column(nullable = false, name = "account_type_code")
	private String accountTypeCode;

	@Column(nullable = false, name = "branch_code")
	private String branchCode;

	@Column(nullable = false, name = "account_number")
	private String accountNumber;

	@Column(nullable = false, name = "password")
	private String password;

	@Column(nullable = false, name = "account_status")
	private AccountStatus accountStatus;

	@Column(nullable = false, name = "daily_transfer_limit")
	private Long dailyTransferLimit;

	@Column(nullable = false, name = "single_transfer_limit")
	private Long singleTransferLimit;

	@Column(nullable = false, name = "available_balance")
	private Long availableBalance;

	@Column(nullable = false, name = "account_balance")
	private Long accountBalance;

	@Column(nullable = false, name = "interest_rate")
	private Double interestRate;

	@Column(name = "account_expiry_date")
	private Date accountExpiryDate;
}
