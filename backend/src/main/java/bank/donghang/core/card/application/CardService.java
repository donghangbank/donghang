package bank.donghang.core.card.application;

import bank.donghang.core.account.application.TransferFacade;
import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.TransferInfo;
import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import bank.donghang.core.card.dto.request.CardTransferRequest;
import bank.donghang.core.card.dto.response.CardTransferResponse;
import org.springframework.stereotype.Service;

import bank.donghang.core.card.domain.repository.CardRepository;
import bank.donghang.core.card.dto.request.CardPasswordRequest;
import bank.donghang.core.card.dto.response.CardPasswordResponse;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {

	private final AccountRepository accountRepository;
	private final CardRepository cardRepository;
	private final TransferFacade transferFacade;

	public CardPasswordResponse checkCardPassword(CardPasswordRequest request) {

		if (!cardRepository.existsByCardNumber(request.cardNumber())) {
			throw new BadRequestException(ErrorCode.CARD_NOT_FOUND);
		}

		CardPasswordResponse response = cardRepository.checkCardPassword(request.cardNumber());

		if (!response.password().equals(request.password())) {
			throw new BadRequestException(ErrorCode.PASSWORD_MISMATCH);
		}

		return response;
	}

	public CardTransferResponse proceedCardTransfer(CardTransferRequest request) {
		Long senderAccountId = cardRepository.findAccountIdByCardNumber(request.cardNumber());

		Account senderAccount = accountRepository.findAccountById(senderAccountId)
				.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));
		Account recipientAccount = accountRepository.findAccountByFullAccountNumber(request.recipientAccountNumber())
						.orElseThrow(() -> new BadRequestException(ErrorCode.ACCOUNT_NOT_FOUND));

		TransferInfo transferInfo = new TransferInfo(
				senderAccount,
				recipientAccount,
				request.amount(),
				request.description(),
				request.sessionStartTime()
		);

		transferFacade.transfer(transferInfo);

		AccountOwnerNameResponse recipientName = accountRepository.getAccountOwnerNameByFullAccountNumber(
				recipientAccount.getAccountTypeCode(),
				recipientAccount.getBranchCode(),
				recipientAccount.getAccountNumber()
		);

		return new CardTransferResponse(
				transferInfo.sendingAccountNumber(),
				transferInfo.receivingAccountNumber(),
				senderAccount.getAccountBalance(),
				recipientName.ownerName(),
				request.amount(),
				TransactionStatus.COMPLETED,
				request.sessionStartTime()
		);
	}
}
