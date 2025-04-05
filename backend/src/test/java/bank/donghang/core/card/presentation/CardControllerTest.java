package bank.donghang.core.card.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.card.dto.request.CardTransferRequest;
import bank.donghang.core.card.dto.response.CardTransferResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.card.application.CardService;
import bank.donghang.core.card.dto.request.CardPasswordRequest;
import bank.donghang.core.card.dto.response.CardPasswordResponse;
import bank.donghang.core.common.controller.ControllerTest;

import java.time.LocalDateTime;

@WebMvcTest(CardController.class)
class CardControllerTest extends ControllerTest {

	@MockitoBean
	private CardService cardService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("카드의 소유자와 비밀번호를 확인할 수 있다.")
	void can_check_card_password() throws Exception {
		Long cardId = 1L;

		var request = new CardPasswordRequest(
			"1234123412341234",
			"1234"
		);

		var expect = new CardPasswordResponse(
			"1234123412341234",
			"1234",
			"110110123456",
			"박종하"
		);

		given(cardService.checkCardPassword(request))
			.willReturn(expect);

		mockMvc.perform(post("/api/v1/cards/check")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("카드로 이체를 할 수 있다.")
	void can_transfer_by_card_number() throws Exception {
		var request = new CardTransferRequest(
				"1234123412341234",
				"110257063096",
				50000L,
				"테스트 이체",
				LocalDateTime.of(1990, 1, 1, 0, 0)
		);

		var expect = new CardTransferResponse(
				"1234123412341234",
				"110257063096",
				950000L,
				"김수한",
				50000L,
				TransactionStatus.COMPLETED,
				LocalDateTime.now()
		);

		given(cardService.proceedCardTransfer(request))
				.willReturn(expect);

		mockMvc.perform(post("/api/v1/cards/transfer")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.sendingCardNumber").value(expect.sendingCardNumber()))
				.andExpect(jsonPath("$.receivingAccountNumber").value(expect.receivingAccountNumber()))
				.andExpect(jsonPath("$.sendingAccountBalance").value(expect.sendingAccountBalance()))
				.andExpect(jsonPath("$.recipientName").value(expect.recipientName()))
				.andExpect(jsonPath("$.amount").value(expect.amount()))
				.andExpect(jsonPath("$.status").value(expect.status().toString()));
	}
}
