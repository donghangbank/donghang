package bank.donghang.core.ledger.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.ledger.domain.enums.EntryType;
import bank.donghang.core.ledger.domain.enums.ReconciliationCode;
import bank.donghang.core.ledger.domain.enums.ReconciliationStatus;
import bank.donghang.core.ledger.domain.repository.LedgerRepository;
import bank.donghang.core.ledger.dto.ErrorDetail;
import bank.donghang.core.ledger.dto.query.DailyReconciliationQuery;
import bank.donghang.core.ledger.dto.response.DailyReconciliationReport;

@ExtendWith(MockitoExtension.class)
class LedgerServiceTest {

	@InjectMocks
	private LedgerService ledgerService;

	@Mock
	private LedgerRepository ledgerRepository;

	@Test
	@DisplayName("정상적인 거래 내역으로 일일 대사 보고서 생성 성공")
	void get_daily_reconciliation_report_success() {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime reportTime = now.minusDays(1);
		LocalDateTime transactionTime = now.minusDays(1).plusHours(2);

		List<DailyReconciliationQuery> mockQueries = List.of(
				createQuery(
						1L,
						TransactionType.DEPOSIT,
						TransactionStatus.COMPLETED,
						1L,
						1L,
						10000L,
						101L,
						EntryType.DEBIT,
						transactionTime
				),
				createQuery(
						1L,
						TransactionType.DEPOSIT,
						TransactionStatus.COMPLETED,
						1L,
						2L,
						10000L,
						201L,
						EntryType.CREDIT,
						transactionTime
				)
		);

		when(ledgerRepository.getDailyReconciliationInfo(any(), any()))
				.thenReturn(mockQueries);
		when(ledgerRepository.updateJournalEntriesStatus(eq(ReconciliationStatus.CONFIRMED), any()))
				.thenReturn(1);

		DailyReconciliationReport report = ledgerService.getDailyReconciliationReport();

		assertNotNull(report);
		assertEquals(1, report.successfulEntries());
		assertEquals(0, report.failedEntries());
		assertEquals(10000L, report.dailyTotalDebit());
		assertEquals(10000L, report.dailyTotalCredit());
		assertTrue(report.errorDetails().isEmpty());
	}

	private static Stream<Arguments> provideIncompleteTransactions() {
		LocalDateTime txTime = LocalDateTime.now().minusHours(3);

		return Stream.of(
				Arguments.of(
						List.of(
								createQuery(
										1L,
										TransactionType.DEPOSIT,
										TransactionStatus.PENDING,
										1L,
										1L,
										10000L,
										101L,
										EntryType.DEBIT,
										txTime
								),
								createQuery(
										1L,
										TransactionType.DEPOSIT,
										TransactionStatus.PENDING,
										1L,
										2L,
										10000L,
										201L,
										EntryType.CREDIT,
										txTime
								)
						),
						ReconciliationCode.TRANSACTION_NOT_COMPLETED
				),
				Arguments.of(
						List.of(
								createQuery(
										2L,
										TransactionType.WITHDRAWAL,
										TransactionStatus.COMPLETED,
										2L,
										3L,
										5000L,
										102L,
										EntryType.DEBIT,
										txTime
								),
								createQuery(
										2L,
										TransactionType.WITHDRAWAL,
										TransactionStatus.COMPLETED,
										2L,
										4L,
										4000L,
										202L,
										EntryType.CREDIT,
										txTime
								)
						),
						ReconciliationCode.AMOUNT_MISMATCH
				),
				Arguments.of(
						List.of(
								createQuery(
										3L,
										TransactionType.TRANSFER,
										TransactionStatus.COMPLETED,
										3L,
										5L,
										10000L,
										103L,
										EntryType.DEBIT,
										txTime
								),
								createQuery(
										3L,
										TransactionType.TRANSFER,
										TransactionStatus.COMPLETED,
										3L,
										6L,
										10000L,
										203L,
										EntryType.DEBIT,
										txTime
								)
						),
						ReconciliationCode.ENTRY_TYPE_MISMATCH
				),
				Arguments.of(
						List.of(
								createQuery(
										4L,
										TransactionType.DEPOSIT,
										TransactionStatus.COMPLETED,
										4L,
										7L,
										10000L,
										104L,
										EntryType.DEBIT,
										txTime
								)
						),
						ReconciliationCode.INVALID_ENTRY_COUNT
				)
		);
	}

	@ParameterizedTest
	@MethodSource("provideIncompleteTransactions")
	@DisplayName("거래 검증 실패 케이스")
	void getDailyReconciliationReport_ShouldFail(
			List<DailyReconciliationQuery> queries,
			ReconciliationCode expectedErrorCode
	) {
		when(ledgerRepository.getDailyReconciliationInfo(any(), any())).thenReturn(queries);
		when(ledgerRepository.updateJournalEntriesStatus(eq(ReconciliationStatus.CONFIRMED), any()))
				.thenReturn(0);

		DailyReconciliationReport report = ledgerService.getDailyReconciliationReport();

		assertThat(report.errorDetails())
				.extracting(ErrorDetail::code)
				.contains(expectedErrorCode);
		assertEquals(0, report.successfulEntries());
		assertEquals(1, report.failedEntries());
	}

	@Test
	@DisplayName("총 차변과 총 대변 불일치 시 검증 실패")
	void getDailyReconciliationReport_TotalBalanceMismatch() {
		LocalDateTime txTime = LocalDateTime.now().minusHours(2);

		List<DailyReconciliationQuery> queries = List.of(
				createQuery(1L, TransactionType.DEPOSIT, TransactionStatus.COMPLETED,
						1L, 1L, 10000L, 101L, EntryType.DEBIT, txTime),
				createQuery(1L, TransactionType.DEPOSIT, TransactionStatus.COMPLETED,
						1L, 2L, 10000L, 201L, EntryType.CREDIT, txTime),
				// 두 번째 거래는 금액 불일치
				createQuery(2L, TransactionType.WITHDRAWAL, TransactionStatus.COMPLETED,
						2L, 3L, 5000L, 102L, EntryType.DEBIT, txTime),
				createQuery(2L, TransactionType.WITHDRAWAL, TransactionStatus.COMPLETED,
						2L, 4L, 4000L, 202L, EntryType.CREDIT, txTime)
		);

		when(ledgerRepository.getDailyReconciliationInfo(any(), any())).thenReturn(queries);
		when(ledgerRepository.updateJournalEntriesStatus(eq(ReconciliationStatus.CONFIRMED), any()))
				.thenReturn(1);

		DailyReconciliationReport report = ledgerService.getDailyReconciliationReport();

		assertEquals(1, report.successfulEntries());
		assertEquals(1, report.failedEntries());
		assertEquals(15000L, report.dailyTotalDebit());
		assertEquals(14000L, report.dailyTotalCredit());

		assertThat(report.errorDetails())
				.extracting(ErrorDetail::code)
				.contains(ReconciliationCode.AMOUNT_MISMATCH, ReconciliationCode.DEBIT_CREDIT_MISMATCH);
	}

	@Test
	@DisplayName("이체 거래에서 엔트리 수 불일치 시 검증 실패")
	void getDailyReconciliationReport_InvalidEntryCountForTransfer() {
		LocalDateTime txTime = LocalDateTime.now().minusHours(5);

		List<DailyReconciliationQuery> queries = List.of(
				createQuery(
						1L,
						TransactionType.TRANSFER,
						TransactionStatus.COMPLETED,
						1L,
						1L,
						10000L,
						101L,
						EntryType.DEBIT,
						txTime
				),
				createQuery(
						1L,
						TransactionType.TRANSFER,
						TransactionStatus.COMPLETED,
						1L,
						2L,
						10000L,
						201L,
						EntryType.CREDIT,
						txTime
				),
				createQuery(
						1L,
						TransactionType.TRANSFER,
						TransactionStatus.COMPLETED,
						1L,
						3L,
						10000L,
						301L,
						EntryType.DEBIT,
						txTime
				)
		);

		when(ledgerRepository.getDailyReconciliationInfo(any(), any())).thenReturn(queries);
		when(ledgerRepository.updateJournalEntriesStatus(eq(ReconciliationStatus.CONFIRMED), any()))
				.thenReturn(0);

		DailyReconciliationReport report = ledgerService.getDailyReconciliationReport();

		assertEquals(0, report.successfulEntries());
		assertEquals(1, report.failedEntries());

		assertThat(report.errorDetails())
				.extracting(ErrorDetail::code)
				.contains(ReconciliationCode.INVALID_ENTRY_COUNT);
	}

	private static DailyReconciliationQuery createQuery(
			Long transactionId,
			TransactionType transactionType,
			TransactionStatus transactionStatus,
			Long journalEntryId,
			Long journalLineId,
			Long amount,
			Long accountId,
			EntryType entryType,
			LocalDateTime transactionTime
	) {
		return new DailyReconciliationQuery(
				transactionId,
				transactionType,
				transactionStatus,
				journalEntryId,
				journalLineId,
				amount,
				accountId,
				entryType,
				transactionTime
		);
	}
}
