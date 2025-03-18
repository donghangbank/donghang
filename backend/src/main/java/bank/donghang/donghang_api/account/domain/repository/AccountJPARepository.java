package bank.donghang.donghang_api.account.domain.repository;

import bank.donghang.donghang_api.account.domain.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountJPARepository extends JpaRepository<Account, Long> {
	Optional<Account> findTopByAccountTypeCodeAndBranchCodeOrderByAccountNumberDesc(
		String accountTypeCode,
		String branchCode
	);
}
