package bank.donghang.core.account.application;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.domain.repository.TransactionRepository;
import bank.donghang.core.account.dto.query.AccountTransactionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerCheckFacade {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public void checkAccount(Account account) {
    }

    public void checkTransaction(
            LocalDateTime start,
            LocalDateTime end
    ) {
        List<AccountTransactionInfo> accountTransactions = transactionRepository.findTransactionsBetweenDates(
                start,
                end
        );
    }
}
