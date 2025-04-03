package bank.donghang.core.ledger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Type;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JournalEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long transactionId;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private LocalDateTime entryDate;

	@Type(JsonType.class)
	@Column(columnDefinition = "json")
	private List<String> ledgerEntryIds;

	public static JournalEntry createJournalEntry(
		Long transactionId,
		String description
	) {
		JournalEntry entry = new JournalEntry();
		entry.transactionId = transactionId;
		entry.description = description;
		entry.entryDate = LocalDateTime.now();
		entry.batchId = UUID.randomUUID().toString();
		return entry;
	}

	// 레저 항목 추가 메서드
	public void addLedgerEntry(Long accountId, LedgerEntry.EntryType entryType, Long amount) {
		this.ledgerEntries.add(new LedgerEntryDTO(accountId, entryType, amount));
	}

	// 분개장 균형 검증 메서드
	public void validateBalanced() {
		long totalDebits = ledgerEntries.stream()
			.filter(e -> e.entryType() == LedgerEntry.EntryType.DEBIT)
			.mapToLong(LedgerEntryDTO::amount)
			.sum();

		long totalCredits = ledgerEntries.stream()
			.filter(e -> e.entryType() == LedgerEntry.EntryType.CREDIT)
			.mapToLong(LedgerEntryDTO::amount)
			.sum();

		if (totalDebits != totalCredits) {
			throw new IllegalStateException("Journal entry is not balanced");
		}
	}
}