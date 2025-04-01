package bank.donghang.core.account.dto.request;

public record AccountPasswordRequest(
	String accountNumber,
	String password
) {
}
