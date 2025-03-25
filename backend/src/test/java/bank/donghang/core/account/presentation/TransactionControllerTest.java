package bank.donghang.core.account.presentation;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.account.application.TransactionService;
import bank.donghang.core.account.domain.enums.TransactionStatus;
import bank.donghang.core.account.domain.enums.TransactionType;
import bank.donghang.core.account.dto.request.DepositRequest;
import bank.donghang.core.account.dto.request.TransactionHistoryRequest;
import bank.donghang.core.account.dto.request.TransactionRequest;
import bank.donghang.core.account.dto.request.WithdrawalRequest;
import bank.donghang.core.account.dto.response.DepositResponse;
import bank.donghang.core.account.dto.response.TransactionHistoryResponse;
import bank.donghang.core.account.dto.response.TransactionResponse;
import bank.donghang.core.account.dto.response.WithdrawalResponse;
import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.common.dto.PageInfo;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest extends ControllerTest {

	@MockitoBean
	private TransactionService transactionService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("계좌간 이체를 한다.")
	public void transfer_by_account() throws Exception {

		String sendingAccountNumber = "1101101234567890";
		String receivingAccountNumber = "1101100987654321";
		Long sendingAccountBalance = 100L;
		Long receivingAccountBalance = 200L;
		Long amount = 100L;
		Long transactionId = 1L;

		var request = new TransactionRequest(
			sendingAccountNumber,
			receivingAccountNumber,
			amount,
			"테스트 이체",
			LocalDateTime.of(2025, 3, 20, 10, 0, 0)
		);

		var expect = new TransactionResponse(
			sendingAccountNumber,
			receivingAccountNumber,
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

		String accountNumber = "1101101234567890";
		Long amount = 100L;
		Long accountBalance = 100L;

		var request = new DepositRequest(
			accountNumber,
			amount,
			LocalDateTime.of(2025, 3, 20, 10, 0, 0)
		);

		var expect = new DepositResponse(
			accountNumber,
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

		String accountNumber = "1101101234567890";
		Long amount = 100L;
		Long accountBalance = 100L;

		var request = new WithdrawalRequest(
			accountNumber,
			amount,
			LocalDateTime.of(2025, 3, 20, 10, 0, 0)
		);

		var expect = new WithdrawalResponse(
			accountNumber,
			amount,
			accountBalance,
			TransactionType.DEPOSIT
		);

		given(transactionService.withdraw(any()))
			.willReturn(expect);

		mockMvc.perform(post("/api/v1/transactions/withdrawal")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("사용자는 계좌의 거래 내역을 조회할 수 있다.")
	void can_get_transaction_histories() throws Exception {
		String accountNumber = "1101101234567890";
		String password = "1234";

		var request = new TransactionHistoryRequest(accountNumber, password);

		List<TransactionHistoryResponse> expectedTransactions = List.of(
			new TransactionHistoryResponse(
				1L,
				LocalDateTime.now().minusHours(2),
				TransactionType.DEPOSIT,
				"Salary deposit",
				1000L,
				1000L
			),
			new TransactionHistoryResponse(
				2L,
				LocalDateTime.now().minusHours(1),
				TransactionType.WITHDRAWAL,
				"ATM withdrawal",
				500L,
				500L
			)
		);

		PageInfo<TransactionHistoryResponse> expectedPage = new PageInfo<>(
			null,
			expectedTransactions,
			false
		);

		given(transactionService.getTransactionHistories(request, null))
			.willReturn(expectedPage);

		MvcResult result = mockMvc.perform(post("/api/v1/transactions/histories")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andReturn();

		var response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<PageInfo<TransactionHistoryResponse>>() {
			}
		);

		Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expectedPage);
	}
}
