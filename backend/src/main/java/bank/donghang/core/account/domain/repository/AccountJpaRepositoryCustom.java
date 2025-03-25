package bank.donghang.core.account.domain.repository;

import bank.donghang.core.account.dto.response.BalanceResponse;

public interface AccountJpaRepositoryCustom {

	BalanceResponse getAccountBalance(
		String accountTypeCode,
		String branchCode,
		String accountNumber
	);
}
