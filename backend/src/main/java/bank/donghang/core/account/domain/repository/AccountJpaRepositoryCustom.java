package bank.donghang.core.account.domain.repository;

import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import bank.donghang.core.account.dto.response.AccountSummaryResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.common.dto.PageInfo;

public interface AccountJpaRepositoryCustom {

	BalanceResponse getAccountBalance(
		String accountTypeCode,
		String branchCode,
		String accountNumber
	);

	PageInfo<AccountSummaryResponse> getAccountSummaries(
		Long memberId, Long cursor
	);

	AccountOwnerNameResponse getAccountOwnerName(
			String accountTypeCode,
			String branchCode,
			String accountNumber
	);
}
