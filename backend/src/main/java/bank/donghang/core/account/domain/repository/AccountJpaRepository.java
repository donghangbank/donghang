package bank.donghang.core.account.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bank.donghang.core.account.domain.Account;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {
	Optional<Account> findTopByAccountTypeCodeAndBranchCodeOrderByAccountNumberDesc(
		String accountTypeCode,
		String branchCode
	);
}
