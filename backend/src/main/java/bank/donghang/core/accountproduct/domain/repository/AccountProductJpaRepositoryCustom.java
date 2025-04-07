package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;

import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;

public interface AccountProductJpaRepositoryCustom {
	List<AccountProductSummary> getAccountProductsByQueryDsl(AccountProductType accountProductType);
}
