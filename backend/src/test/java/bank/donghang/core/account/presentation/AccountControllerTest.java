package bank.donghang.core.account.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.account.application.AccountService;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.common.controller.ControllerTest;

@WebMvcTest(AccountController.class)
class AccountControllerTest extends ControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AccountService accountService;

	@Test
	@DisplayName("자유입출금 계좌 가입 요청 성공 시, AccountRegisterResponse 반환")
	void registerDemandAccount_success() throws Exception {
		Long memberId = 1L;
		Long accountProductId = 2L;
		String password = "password123";

		DemandAccountRegisterRequest request = new DemandAccountRegisterRequest(
			memberId,
			accountProductId,
			password
		);

		AccountRegisterResponse response = new AccountRegisterResponse(
			"Savings Product",
			null,
			null,
			"100001000001",
			0L,
			0.05,
			LocalDate.of(2025, 3, 20)
		);

		Mockito.when(accountService.createDemandAccount(any(DemandAccountRegisterRequest.class)))
			.thenReturn(response);

		mockMvc.perform(
				post("/api/v1/accounts/demands")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("Savings Product"))
			.andExpect(jsonPath("$.accountNumber").value("100001000001"))
			.andExpect(jsonPath("$.accountBalance").value(0))
			.andExpect(jsonPath("$.interestDate").value(0.05))
			.andDo(document("demand-account-register"));
	}

	@Test
	@DisplayName("예금 상품 가입 요청 성공 시, AccountRegisterResponse 반환")
	void registerDepositAccount_success() throws Exception {
		Long memberId = 1L;
		Long accountProductId = 2L;
		String password = "password123";
		String withdrawalAccountNumber = "100001000123";
		String payoutAccountNumber = "100001000456";
		Long initialDepositAmount = 30_000L;

		DepositAccountRegisterRequest request = new DepositAccountRegisterRequest(
			memberId,
			accountProductId,
			password,
			withdrawalAccountNumber,
			payoutAccountNumber,
			initialDepositAmount
		);

		AccountRegisterResponse response = new AccountRegisterResponse(
			"Deposit Product",
			withdrawalAccountNumber,
			payoutAccountNumber,
			"200001000001",
			0L,
			0.5,
			LocalDate.of(2025, 3, 20)
		);

		Mockito.when(accountService.createDepositAccount(any(DepositAccountRegisterRequest.class)))
			.thenReturn(response);

		mockMvc.perform(
				post("/api/v1/accounts/deposits")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("Deposit Product"))
			.andExpect(jsonPath("$.accountNumber").value("200001000001"))
			.andExpect(jsonPath("$.accountBalance").value(0))
			.andExpect(jsonPath("$.interestDate").value(0.5))
			.andExpect(jsonPath("$.accountExpiryDate").exists())
			.andDo(document("deposit-account-register"));
	}

	@Test
	@DisplayName("적금 상품 가입 요청 성공 시, AccountRegisterResponse 반환")
	void registerInstallmentAccount_success() throws Exception {
		Long memberId = 1L;
		Long accountProductId = 3L;
		String password = "password123";
		String withdrawalAccountNumber = "100001000123";
		String payoutAccountNumber = "100001000456";
		Long monthlyInstallmentAmount = 30_000L;
		Integer monthlyInstallmentDay = 20;

		InstallmentAccountRegisterRequest request = new InstallmentAccountRegisterRequest(
			memberId,
			accountProductId,
			password,
			withdrawalAccountNumber,
			payoutAccountNumber,
			monthlyInstallmentAmount,
			monthlyInstallmentDay
		);

		AccountRegisterResponse response = new AccountRegisterResponse(
			"Installment Product",
			withdrawalAccountNumber,
			payoutAccountNumber,
			"300001000001",
			0L,
			5.0,
			LocalDate.of(2027, 3, 25)
		);

		Mockito.when(accountService.createInstallmentAccount(any(InstallmentAccountRegisterRequest.class)))
			.thenReturn(response);

		mockMvc.perform(
				post("/api/v1/accounts/installments")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("Installment Product"))
			.andExpect(jsonPath("$.accountNumber").value("300001000001"))
			.andExpect(jsonPath("$.accountBalance").value(0))
			.andExpect(jsonPath("$.interestDate").value(5.0))
			.andExpect(jsonPath("$.accountExpiryDate").exists())
			.andDo(document("installment-account-register"));
	}
}
