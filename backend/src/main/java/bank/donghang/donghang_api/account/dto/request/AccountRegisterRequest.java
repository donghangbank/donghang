package bank.donghang.donghang_api.account.dto.request;

import bank.donghang.donghang_api.account.domain.Account;
import bank.donghang.donghang_api.account.domain.enums.AccountStatus;

public record AccountRegisterRequest(
        Long memberId,
        Long accountProductId,
        String password
) {
    // todo. password encoding
    // todo. account type handling logic
    public Account toEntity(String generatedAccountNumber, Double interestRate) {
        return Account.builder()
                .memberId(memberId)
                .accountProductId(accountProductId)
                .password(password)
                .accountTypeCode("100")
                .branchCode("001")
                .accountNumber(generatedAccountNumber)
                .accountStatus(AccountStatus.ACTIVE)
                .dailyTransferLimit(100_000_000L)
                .singleTransferLimit(10_000_000L)
                .availableBalance(0L)
                .accountBalance(0L)
                .interestRate(interestRate)
                .build();
    }
}