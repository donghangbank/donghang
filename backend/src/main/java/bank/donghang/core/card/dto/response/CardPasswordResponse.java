package bank.donghang.core.card.dto.response;

public record CardPasswordResponse(
	String cardNumber,
	String password,
	String fullAccountNumber,
	String ownerName,
	Long ownerId
) {
}
