package bank.donghang.core.account.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import bank.donghang.core.account.dto.query.AccountTransactionInfo;
import bank.donghang.core.account.dto.response.TransactionHistoryResponse;

public interface TransactionJpaRepositoryCustom {

	List<TransactionHistoryResponse> getTransactionHistoriesByFullAccountNumber(
		String accountTypeCode,
		String branchCode,
		String accountNumber,
		String pageToken,
		int pageSize
	);

	List<AccountTransactionInfo> getTransactionsBetweenDates(
			LocalDateTime start,
			LocalDateTime end
	);
}

