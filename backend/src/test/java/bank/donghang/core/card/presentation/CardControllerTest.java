package bank.donghang.core.card.presentation;

import bank.donghang.core.card.application.CardService;
import bank.donghang.core.card.dto.request.CardPasswordRequest;
import bank.donghang.core.card.dto.response.CardPasswordResponse;
import bank.donghang.core.common.controller.ControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                1L,
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
}