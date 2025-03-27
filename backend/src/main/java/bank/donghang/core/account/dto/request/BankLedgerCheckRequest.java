package bank.donghang.core.account.dto.request;

import java.time.LocalDateTime;

public record BankLedgerCheckRequest (
        LocalDateTime checkRequestTime
) {
}
