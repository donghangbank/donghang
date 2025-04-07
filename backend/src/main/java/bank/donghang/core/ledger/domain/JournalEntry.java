package bank.donghang.core.ledger.domain;

import org.hibernate.annotations.Type;

import bank.donghang.core.account.domain.enums.TransactionType;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "journal_type")
	private TransactionType journalType;

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private JournalEntryInfo journalEntryInfo;

	public void updateReconciliationStatus(ReconciliationStatus status) {
		this.reconciliationStatus = status;
	}

	public static JournalEntry create(
		Long transactionId,
		ReconciliationStatus reconciliationStatus,
		JournalEntryInfo journalEntryInfo,
		TransactionType journalType
	) {
		return new JournalEntry(
			transactionId,
			reconciliationStatus,
			journalEntryInfo,
			journalType
		);
	}

	private JournalEntry(
		Long transactionId,
		ReconciliationStatus reconciliationStatus,
		JournalEntryInfo journalEntryInfo,
		TransactionType journalType
	) {
		this.transactionId = transactionId;
		this.reconciliationStatus = reconciliationStatus;
		this.journalEntryInfo = journalEntryInfo;
		this.journalType = journalType;
	}
}
