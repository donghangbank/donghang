package bank.donghang.core.card.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.card.domain.repository.CardRepository;
import bank.donghang.core.card.dto.request.CardPasswordRequest;
import bank.donghang.core.card.dto.response.CardPasswordResponse;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

	@InjectMocks
	private CardService cardService;

	@Mock
	private CardRepository cardRepository;

	@Test
	@DisplayName("카드 번호와 비밀번호를 통해 카드의 소유주와 출금 계좌를 확인할 수 있다.")
	void can_check_card_password() {

		String cardNumber = "123123213";
		String password = "1234";
		String fullAccountNumber = "110257063096";
		String name = "박종하";

		var request = new CardPasswordRequest(
			cardNumber,
			password
		);

		var expect = new CardPasswordResponse(
			cardNumber,
			password,
			fullAccountNumber,
			name,
			1L
		);

		given(cardRepository.existsByCardNumber(cardNumber))
			.willReturn(true);
		given(cardRepository.checkCardPassword(cardNumber))
			.willReturn(expect);

		CardPasswordResponse response = cardService.checkCardPassword(request);

		Assertions.assertEquals(expect, response);
		verify(cardRepository).existsByCardNumber(cardNumber);
		verify(cardRepository).checkCardPassword(cardNumber);
	}

	@Test
	@DisplayName("비밀번호가 틀리면 카드 소유주와 출금 계좌를 확인할 수 없다.")
	void can_not_check_card_password_if_password_is_wrong() {

		String cardNumber = "123123213";
		String correctPassword = "1234";
		String wrongPassword = "4321";
		String fullAccountNumber = "110257063096";
		String name = "박종하";

		var request = new CardPasswordRequest(cardNumber, wrongPassword);
		var mockResponse = new CardPasswordResponse(
			cardNumber,
			correctPassword,
			fullAccountNumber,
			name,
			1L
		);

		given(cardRepository.existsByCardNumber(cardNumber))
			.willReturn(true);
		given(cardRepository.checkCardPassword(cardNumber))
			.willReturn(mockResponse);

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> cardService.checkCardPassword(request));

		assertEquals(ErrorCode.PASSWORD_MISMATCH.getMessage(), exception.getMessage());
		verify(cardRepository).existsByCardNumber(cardNumber);
		verify(cardRepository).checkCardPassword(cardNumber);
	}

	@Test
	@DisplayName("존재하지 않는 카드 번호로 요청하면 예외가 발생한다.")
	void should_throw_exception_when_card_not_found() {
		String nonExistentCardNumber = "999999999";
		String password = "1234";

		var request = new CardPasswordRequest(nonExistentCardNumber, password);

		given(cardRepository.existsByCardNumber(nonExistentCardNumber)).willReturn(false);

		BadRequestException exception = assertThrows(BadRequestException.class,
			() -> cardService.checkCardPassword(request));

		assertEquals(ErrorCode.CARD_NOT_FOUND.getMessage(), exception.getMessage());
		verify(cardRepository).existsByCardNumber(nonExistentCardNumber);
		verify(cardRepository, never()).checkCardPassword(any());
	}
}
