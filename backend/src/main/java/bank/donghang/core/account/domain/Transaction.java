package bank.donghang.core.account.domain;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Transaction extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	private Long sendingAccountId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TransactionStatus status;

	public void complete() {
		this.status = TransactionStatus.COMPLETED;
	}

	public static Transaction createTransaction(
		String description,
		Long amount,
		Long sendingAccountId,
		TransactionType type,
		TransactionStatus status
	) {
		return new Transaction(
			description,
			amount,
			sendingAccountId,
			type,
			status
		);
	}

	private Transaction(
		String description,
		Long amount,
		Long sendingAccountId,
		TransactionType type,
		TransactionStatus status
	) {
		this.description = description;
		this.amount = amount;
		this.sendingAccountId = sendingAccountId;
		this.type = type;
		this.status = status;
	}

}
