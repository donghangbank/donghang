package bank.donghang.core.account.dto.query;

import java.time.LocalDateTime;
import java.util.List;

public record WrongAccountInfo(
        Long accountId,
        Long amount,
        LocalDateTime checkedAt,
        List<Long> transactionWithProblem
) {
}
