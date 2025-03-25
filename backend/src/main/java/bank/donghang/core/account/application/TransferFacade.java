package bank.donghang.core.account.application;

import bank.donghang.core.account.domain.TransferCommand;
import bank.donghang.core.account.dto.response.TransactionResponse;

public interface TransferFacade {
	void transfer(TransferCommand command);
}
