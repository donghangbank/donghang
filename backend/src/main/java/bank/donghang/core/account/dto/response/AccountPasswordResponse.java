package bank.donghang.core.account.dto.response;

public record AccountPasswordResponse(
	String fullAccountNumber,
	String password,
	String ownerName
) {
}
