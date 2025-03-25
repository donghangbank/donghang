package bank.donghang.core.account.domain.repository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.account.dto.response.TransactionHistoryResponse;
import bank.donghang.core.common.dto.PageInfo;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
	private static final int TRANSACTION_PAGE_SIZE = 30;

	private final TransactionJpaRepository transactionJpaRepository;
	private final TransactionJpaRepositoryCustomImpl transactionJpaRepositoryCustomImpl;

	public Transaction saveTransaction(Transaction transaction) {
		return transactionJpaRepository.save(transaction);
	}

	public boolean existsByAccountIdAndAmountAndTypeAndSessionStartTime(
		Long accountId,
		Long amount,
		LocalDateTime sessionStartTime,
		TransactionType type
	) {
		return transactionJpaRepository.existsByAccountIdAndAmountAndTypeAndSessionStartTime(
			accountId,
			amount,
			type,
			sessionStartTime
		);
	}

	public PageInfo<TransactionHistoryResponse> getTransactionHistoryByFullAccountNumber(
		String fullAccountNumber,
		String pageToken
	) {
		String accountTypeCode = fullAccountNumber.substring(0, 3);
		String branchCode = fullAccountNumber.substring(3, 6);
		String accountNumber = fullAccountNumber.substring(6);

		var data = transactionJpaRepositoryCustomImpl.getTransactionHistoriesByFullAccountNumber(
			accountTypeCode,
			branchCode,
			accountNumber,
			pageToken,
			TRANSACTION_PAGE_SIZE
		);

		if (data.size() <= TRANSACTION_PAGE_SIZE) {
			return PageInfo.of(null, data, false);
		}

		var lastData = data.get(data.size() - 1);
		data.remove(data.size() - 1);
		String nextPageToken = String.valueOf(lastData.transactionId());

		return PageInfo.of(nextPageToken, data, true);
	}
}
