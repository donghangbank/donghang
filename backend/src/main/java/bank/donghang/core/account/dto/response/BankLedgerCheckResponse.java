package bank.donghang.core.account.dto.response;

import bank.donghang.core.account.domain.enums.LedgerCheckStatus;
import bank.donghang.core.account.dto.query.WrongAccountInfo;

import java.time.LocalDateTime;
import java.util.List;

public record BankLedgerCheckResponse (
        LedgerCheckStatus checkStatus,
        List<WrongAccountInfo> accountWithProblem,
        LocalDateTime checkedAt
) {
}
