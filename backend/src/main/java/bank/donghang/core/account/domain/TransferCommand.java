package bank.donghang.core.account.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record TransferCommand(
	Account sendingAccount,
	Account receivingAccount,
	Long amount, String description,
	LocalDateTime sessionStartTime) {
}
