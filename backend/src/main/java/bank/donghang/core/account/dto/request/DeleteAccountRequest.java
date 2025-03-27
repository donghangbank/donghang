package bank.donghang.core.account.dto.request;

public record DeleteAccountRequest(
	String fullAccountNumber,
	String password
) {
}
