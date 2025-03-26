package bank.donghang.core.account.dto;

import java.time.LocalDateTime;

import bank.donghang.core.account.domain.Account;
import lombok.Builder;

@Builder
public record TransferInfo(
	Account sendingAccount,
	Account receivingAccount,
	Long amount, String description,
	LocalDateTime sessionStartTime) {
	public String sendingAccountNumber() {

		return sendingAccount.getAccountTypeCode()
			+ sendingAccount.getBranchCode()
			+ sendingAccount.getAccountNumber();
	}

	public String receivingAccountNumber() {

		return receivingAccount.getAccountTypeCode()
			+ receivingAccount.getBranchCode()
			+ receivingAccount.getAccountNumber();
	}
}
