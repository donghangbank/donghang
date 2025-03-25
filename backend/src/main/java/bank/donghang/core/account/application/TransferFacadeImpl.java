package bank.donghang.core.account.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.Transaction;
import bank.donghang.core.account.domain.TransferCommand;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferFacadeImpl implements TransferFacade {
	TransactionService transactionService;
	AccountRepository accountRepository;

	@Override
	@Transactional
	public void transfer(TransferCommand command) {
		long sendingAccountId = command.sendingAccountId();
		long receivingAccountId = command.receivingAccountId();

		Account sendingAccount = accountRepository.findAccountById(sendingAccountId)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));
		Account receivingAccount = accountRepository.findAccountById(receivingAccountId)
			.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		String description = command.description();
		long amount = command.amount();
		transactionService.transfer(sendingAccount, receivingAccount, description, amount, LocalDateTime.now());
	}
}
