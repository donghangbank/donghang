package bank.donghang.core.ledger.domain;

import org.hibernate.annotations.Where;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ledger extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "sender_account_id")
	private Long senderAccountId;

	@Column(nullable = false, name = "recipient_account_id")
	private Long recipientAccountId;

	@Column(nullable = false, name = "deposit_amount")
	private Long depositAmount;

	@Column(nullable = false, name = "withdrawal_amount")
	private Long withdrawalAmount;

	@Column(nullable = false, name = "status")
	private TransactionStatus status;

	public static Ledger createLedger(
		Long senderAccountId,
		Long recipientAccountId,
		Long depositAmount,
		Long withdrawalAmount,
		TransactionStatus status
	) {
		return new Ledger(
			senderAccountId,
			recipientAccountId,
			depositAmount,
			withdrawalAmount,
			status
		);
	}

	private Ledger(
		Long senderAccountId,
		Long recipientAccountId,
		Long depositAmount,
		Long withdrawalAmount,
		TransactionStatus status
	) {
		this.senderAccountId = senderAccountId;
		this.recipientAccountId = recipientAccountId;
		this.depositAmount = depositAmount;
		this.withdrawalAmount = withdrawalAmount;
		this.status = status;
	}
}
