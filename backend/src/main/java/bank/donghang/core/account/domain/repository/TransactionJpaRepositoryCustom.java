package bank.donghang.core.account.domain.repository;

import java.util.List;

import bank.donghang.core.account.dto.response.TransactionHistoryResponse;

public interface TransactionJpaRepositoryCustom {

	List<TransactionHistoryResponse> getTransactionHistoriesByFullAccountNumber(
		String accountTypeCode,
		String branchCode,
		String accountNumber,
		String pageToken,
		int pageSize
	);
}

