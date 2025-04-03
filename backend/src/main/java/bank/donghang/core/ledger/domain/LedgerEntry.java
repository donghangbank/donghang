package bank.donghang.core.account.domain;

import bank.donghang.core.ledger.domain.enums.EntryType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LedgerEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "journal_entry_id")
	private Long journalEntryId;

	@Column(nullable = false, name = "account_id")
	private Long accountId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EntryType entryType;

	@Column(nullable = false)
	private Long amount;

	public static LedgerEntry createLedgerEntry(
		Long accountId,
		EntryType entryType,
		Long amount
	) {
		LedgerEntry entry = new LedgerEntry();
		entry.accountId = accountId;
		entry.entryType = entryType;
		entry.amount = amount;
		return entry;
	}
}