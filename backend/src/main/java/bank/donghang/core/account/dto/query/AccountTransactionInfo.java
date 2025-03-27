package bank.donghang.core.account.dto.query;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;

public record AccountTransactionInfo(
        Long accountId,
        Long amount,
        TransactionStatus status,
        TransactionType type
) {
}
