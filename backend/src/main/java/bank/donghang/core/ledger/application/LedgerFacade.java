package bank.donghang.core.ledger.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import bank.donghang.core.ledger.domain.repository.LedgerRepository;
import bank.donghang.core.ledger.dto.query.TransactionSumDay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LedgerFacade {

	private final LedgerRepository ledgerRepository;

	public TransactionSumDay checkOneDayTransactionLedger(
		LocalDateTime start,
		LocalDateTime end
	) {
		TransactionSumDay depositSum = ledgerRepository.getTransactionDepositSumDay(
			start,
			end
		);

		TransactionSumDay withdrawSum = ledgerRepository.getTransactionWithdrawalSumDay(
			start,
			end
		);

		return withdrawSum;
	}
}
