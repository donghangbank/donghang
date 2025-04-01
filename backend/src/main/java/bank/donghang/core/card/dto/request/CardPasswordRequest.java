package bank.donghang.core.card.dto.request;

public record CardPasswordRequest(
        String cardNumber,
        String password
) {
}
