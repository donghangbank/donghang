package bank.donghang.core.account.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.account.application.AccountService;
import bank.donghang.core.account.dto.request.AccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.common.controller.ControllerTest;

@WebMvcTest(AccountController.class)
class AccountControllerTest extends ControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AccountService accountService;

	@Test
	@DisplayName("계좌 등록 요청 성공 시, AccountRegisterResponse 반환")
	void registerAccount_success() throws Exception {
		// Arrange
		Long memberId = 1L;
		Long accountProductId = 2L;
		String password = "password123";
		AccountRegisterRequest request = new AccountRegisterRequest(memberId, accountProductId, password);

		AccountRegisterResponse response = new AccountRegisterResponse("Savings Account", "WID123", "1000011234567890",
			0L, 0.05, new Date(1680105600000L));

		Mockito.when(accountService.createAccount(any(AccountRegisterRequest.class))).thenReturn(response);

		// Act & Assert
		mockMvc.perform(post("/api/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("Savings Account"))
			.andExpect(jsonPath("$.withdrawalAccountId").value("WID123"))
			.andExpect(jsonPath("$.accountNumber").value("1000011234567890"))
			.andExpect(jsonPath("$.accountBalance").value(0))
			.andExpect(jsonPath("$.interestDate").value(0.05))
			.andExpect(jsonPath("$.accountExpiryDate").exists())

			.andDo(document("account-register"));
	}
}
