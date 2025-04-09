package bank.donghang.core.ledger.domain.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import bank.donghang.core.ledger.domain.JournalLine;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JournalLineJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void batchInsert(List<JournalLine> journalLines) {
		String sql = "INSERT INTO journal_line "
			+ "(journal_entry_id, account_id, entry_type, amount, created_at, modified_at) "
			+ "VALUES (?, ?, ?, ?, NOW(), NOW())";

		jdbcTemplate.batchUpdate(sql,
			new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int idx) throws SQLException {
					JournalLine journalLine = journalLines.get(idx);
					ps.setLong(1, journalLine.getJournalEntryId());
					ps.setLong(2, journalLine.getAccountId());
					ps.setString(3, journalLine.getEntryType().toString());
					ps.setDouble(4, journalLine.getAmount());
				}

				@Override
				public int getBatchSize() {
					return journalLines.size();
				}
			});
	}
}
