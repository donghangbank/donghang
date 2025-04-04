package bank.donghang.core.ledger.application;

import org.springframework.stereotype.Service;

import bank.donghang.core.ledger.domain.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerService {

	private final LedgerRepository ledgerRepository;
}
