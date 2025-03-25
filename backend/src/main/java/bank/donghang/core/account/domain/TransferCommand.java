package bank.donghang.core.account.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
public record TransferCommand(
	long sendingAccountId,
	long receivingAccountId,
	Long amount, String description,
	LocalDateTime sessionStartTime) {
}
