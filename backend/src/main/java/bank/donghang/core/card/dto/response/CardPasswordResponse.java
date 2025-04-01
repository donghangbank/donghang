package bank.donghang.core.card.dto.response;

public record CardPasswordResponse(String cardNumber, String password, Long accountId, String ownerName) {
}
