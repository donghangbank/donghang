package bank.donghang.core.ledger.domain;

import bank.donghang.core.common.entity.BaseEntity;
import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
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
public class JournalEntry extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "transaction_id", nullable = false)
	private Long transactionId;

	@Enumerated(EnumType.STRING)
	@Column(name = "reconciliation_status", nullable = false)
	private ReconciliationStatus reconciliationStatus;

	@Column(nullable = false, name = "description", length = 256)
	private String description;

	public static JournalEntry create(
		ReconciliationStatus reconciliationStatus,
		String description
	) {
		return new JournalEntry(
			reconciliationStatus,
			description
		);
	}

	private JournalEntry(
		ReconciliationStatus reconciliationStatus,
		String description
	) {
		this.reconciliationStatus = reconciliationStatus;
		this.description = description;
	}
}
