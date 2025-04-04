package bank.donghang.core.ledger.domain;

import bank.donghang.core.common.entity.BaseEntity;
import bank.donghang.core.ledger.domain.enums.EntryType;
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
public class JournalLine extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "journal_entry_id", nullable = false)
	private Long journalEntryId;

	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "entry_type", nullable = false)
	private EntryType entryType;

	@Column(name = "amount", nullable = false)
	private Long amount;

	public static JournalLine create(
		Long journalEntryId,
		Long accountId,
		EntryType entryType,
		Long amount
	) {
		return new JournalLine(
			journalEntryId,
			accountId,
			entryType,
			amount
		);
	}

	private JournalLine(
		Long journalEntryId,
		Long accountId,
		EntryType entryType,
		Long amount
	) {
		this.journalEntryId = journalEntryId;
		this.accountId = accountId;
		this.entryType = entryType;
		this.amount = amount;
	}
}
