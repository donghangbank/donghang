package bank.donghang.core.account.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bank.donghang.core.account.domain.Account;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {
	Optional<Account> findTopByAccountTypeCodeAndBranchCodeOrderByAccountNumberDesc(
		String accountTypeCode,
		String branchCode
	);

	Optional<Account> findByAccountTypeCodeAndBranchCodeAndAccountNumber(
		String typeCode,
		String branchCode,
		String accountCode
	);

	boolean existsByAccountTypeCodeAndBranchCodeAndAccountNumber(
		String accountTypeCode,
		String branchCode,
		String accountNumber
	);
}
