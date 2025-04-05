package bank.donghang.core.card.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bank.donghang.core.card.application.CardService;
import bank.donghang.core.card.dto.request.CardPasswordRequest;
import bank.donghang.core.card.dto.request.CardTransferRequest;
import bank.donghang.core.card.dto.response.CardPasswordResponse;
import bank.donghang.core.card.dto.response.CardTransferResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardController {
	private final CardService cardService;

	@PostMapping("/check")
	public ResponseEntity<CardPasswordResponse> checkCardPassword(@RequestBody CardPasswordRequest request) {
		CardPasswordResponse response = cardService.checkCardPassword(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/transfer")
	public ResponseEntity<CardTransferResponse> proceedCardTransfer(@RequestBody CardTransferRequest request) {
		CardTransferResponse response = cardService.proceedCardTransfer(request);

		return ResponseEntity.ok(response);
	}
}
