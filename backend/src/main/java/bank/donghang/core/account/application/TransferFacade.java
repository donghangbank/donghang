package bank.donghang.core.account.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.TransferCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferFacade {
	TransactionService transactionService;

	@Transactional
	public void transfer(TransferCommand command) {
		Account sendingAccount = command.sendingAccount();
		Account receivingAccount = command.receivingAccount();
		String description = command.description();
		long amount = command.amount();
		transactionService.transfer(sendingAccount, receivingAccount, description, amount, LocalDateTime.now());
	}
}
