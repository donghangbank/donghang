package bank.donghang.core.ledger.domain;

import org.hibernate.annotations.Type;

import bank.donghang.core.common.entity.BaseEntity;
import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
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

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private JournalEntryInfo journalEntryInfo;

	public static JournalEntry create(
		Long transactionId,
		ReconciliationStatus reconciliationStatus,
		JournalEntryInfo journalEntryInfo
	) {
		return new JournalEntry(
			transactionId,
			reconciliationStatus,
			journalEntryInfo
		);
	}

	private JournalEntry(
		Long transactionId,
		ReconciliationStatus reconciliationStatus,
		JournalEntryInfo journalEntryInfo
	) {
		this.transactionId = transactionId;
		this.reconciliationStatus = reconciliationStatus;
		this.journalEntryInfo = journalEntryInfo;
	}
}
