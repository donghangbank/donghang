package bank.donghang.core.account.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.account.application.TransactionService;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.account.dto.request.DepositRequest;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.request.WithdrawalRequest;
import bank.donghang.core.account.dto.response.DepositResponse;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.account.dto.response.WithdrawalResponse;
import bank.donghang.core.common.controller.ControllerTest;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest extends ControllerTest {

	@MockitoBean
	private TransactionService transactionService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("계좌간 이체를 한다.")
	public void transfer_by_account() throws Exception {

		Long sendingAccountId = 1L;
		Long receivingAccountId = 2L;
		Long sendingAccountBalance = 100L;
		Long receivingAccountBalance = 200L;
		Long amount = 100L;
		Long transactionId = 1L;

		var request = new TransactionRequest(
			sendingAccountId,
			receivingAccountId,
			amount,
			"테스트 이체"
		);

		var expect = new TransactionResponse(
			sendingAccountId,
			receivingAccountBalance + amount,
			sendingAccountBalance + amount,
			amount,
			TransactionStatus.COMPLETED,
			transactionId
		);

		given(transactionService.transferByAccount(any()))
			.willReturn(expect);

		mockMvc.perform(post("/api/v1/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("계좌에서 입금을 할 수 있다.")
	public void deposit_by_account() throws Exception {

		Long accountId = 1L;
		Long amount = 100L;
		Long accountBalance = 100L;

		var request = new DepositRequest(
			accountId,
			amount
		);

		var expect = new DepositResponse(
			accountId,
			amount,
			accountBalance,
			TransactionType.DEPOSIT
		);

		given(transactionService.deposit(any()))
			.willReturn(expect);

		mockMvc.perform(post("/api/v1/transactions/deposit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("계좌에서 입금을 할 수 있다.")
	public void withdrawal_by_account() throws Exception {

		Long accountId = 1L;
		Long amount = 100L;
		Long accountBalance = 100L;

		var request = new WithdrawalRequest(
			accountId,
			amount
		);

		var expect = new WithdrawalResponse(
			accountId,
			amount,
			accountBalance,
			TransactionType.WITHDRAWAL
		);

		given(transactionService.withdraw(any()))
			.willReturn(expect);

		mockMvc.perform(post("/api/v1/transactions/withdrawal")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk());
	}
}
