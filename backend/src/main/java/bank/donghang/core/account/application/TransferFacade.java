package bank.donghang.core.account.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.dto.TransferInfo;
import bank.donghang.core.common.annotation.TransferDistributedLock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferFacade {
	private final TransactionService transactionService;

	@TransferDistributedLock(
		key1 = "#request.sendingAccountNumber()",
		key2 = "#request.receivingAccountNumber()",
		waitTime = 1L,
		leaseTime = 3L
	)
	@Transactional
	public void transfer(TransferInfo request) {
		Account sendingAccount = request.sendingAccount();
		Account receivingAccount = request.receivingAccount();

		String description = request.description();
		long amount = request.amount();
		transactionService.transfer(
				sendingAccount,
				receivingAccount,
				description,
				amount,
				LocalDateTime.now()
		);
	}
}
