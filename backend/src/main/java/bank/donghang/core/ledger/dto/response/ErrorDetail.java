package bank.donghang.core.ledger.dto.response;

public record ErrorDetail(
	Long entryId,
	String errorMessage
) {
}
