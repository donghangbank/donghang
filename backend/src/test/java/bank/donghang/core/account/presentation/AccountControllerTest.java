package bank.donghang.core.account.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.donghang.core.account.application.AccountService;
import bank.donghang.core.account.dto.request.AccountOwnerNameRequest;
import bank.donghang.core.account.dto.request.BalanceRequest;
import bank.donghang.core.account.dto.request.DeleteAccountRequest;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.request.MyAccountsRequest;
import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.account.dto.response.AccountSummaryResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.common.dto.PageInfo;

@WebMvcTest(AccountController.class)
class AccountControllerTest extends ControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private AccountService accountService;

	@Test
	@DisplayName("사용자는 자신의 계좌 목록을 조회할 경우, PageInfo<AccountSummaryResponse> 반환")
	void getMyAccounts_success() throws Exception {
		// given
		Long memberId = 1L;
		String pageToken = "123";

		// 실제로 반환될 계좌 요약 정보 리스트
		List<AccountSummaryResponse> accounts = List.of(
			new AccountSummaryResponse("테스트은행", "100-456-789000", AccountProductType.DEMAND, 10000L),
			new AccountSummaryResponse("또다른은행", "200-654-321000", AccountProductType.DEPOSIT, 20000L)
		);

		// PageInfo에 계좌 리스트와 다음 페이지 유무(hasNext)를 설정
		PageInfo<AccountSummaryResponse> pageInfo = PageInfo.of("456", accounts, true);

		// Service mock 설정
		given(accountService.getMyAccounts(any(MyAccountsRequest.class), eq(pageToken)))
			.willReturn(pageInfo);

		// when & then
		mockMvc.perform(post("/api/v1/accounts/me")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new MyAccountsRequest(memberId)))
				.param("pageToken", pageToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.pageToken").value("456"))
			.andExpect(jsonPath("$.hasNext").value(true))

			// data[0] 검증
			.andExpect(jsonPath("$.data[0].bankName").value("테스트은행"))
			.andExpect(jsonPath("$.data[0].accountNumber").value("100-456-789000")) // 마스킹 로직이 있다면 이 값이 실제로 어떻게 나오는지도 확인
			.andExpect(jsonPath("$.data[0].accountProductType").value("DEMAND"))
			.andExpect(jsonPath("$.data[0].balance").value(10000))

			// data[1] 검증
			.andExpect(jsonPath("$.data[1].bankName").value("또다른은행"))
			.andExpect(jsonPath("$.data[1].accountNumber").value("200-654-321000"))
			.andExpect(jsonPath("$.data[1].accountProductType").value("DEPOSIT"))
			.andExpect(jsonPath("$.data[1].balance").value(20000));
	}

	@Test
	@DisplayName("자유입출금 계좌 가입 요청 성공 시, AccountRegisterResponse 반환")
	void register_demand_account_success() throws Exception {
		Long memberId = 1L;
		Long accountProductId = 2L;
		String password = "password123";

		DemandAccountRegisterRequest request = new DemandAccountRegisterRequest(memberId, accountProductId, password,
			true);

		AccountRegisterResponse response = new AccountRegisterResponse("Savings Product", null, null, "100001000001",
			0L, 0.05, LocalDate.of(2025, 3, 20));

		when(accountService.createDemandAccount(any(DemandAccountRegisterRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/accounts/demands").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("Savings Product"))
			.andExpect(jsonPath("$.accountNumber").value("100001000001"))
			.andExpect(jsonPath("$.accountBalance").value(0))
			.andExpect(jsonPath("$.interestRate").value(0.05))
			.andDo(document("demand-account-register"));
	}

	@Test
	@DisplayName("예금 상품 가입 요청 성공 시, AccountRegisterResponse 반환")
	void register_deposit_account_success() throws Exception {
		Long memberId = 1L;
		Long accountProductId = 2L;
		String password = "password123";
		String withdrawalAccountNumber = "100001000123";
		String payoutAccountNumber = "100001000456";
		Long initialDepositAmount = 30_000L;

		DepositAccountRegisterRequest request = new DepositAccountRegisterRequest(memberId, accountProductId, password,
			withdrawalAccountNumber, payoutAccountNumber, initialDepositAmount, true);

		AccountRegisterResponse response = new AccountRegisterResponse("Deposit Product", withdrawalAccountNumber,
			payoutAccountNumber, "200001000001", 0L, 0.5, LocalDate.of(2025, 3, 20));

		when(accountService.createDepositAccount(any(DepositAccountRegisterRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/accounts/deposits").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("Deposit Product"))
			.andExpect(jsonPath("$.accountNumber").value("200001000001"))
			.andExpect(jsonPath("$.accountBalance").value(0))
			.andExpect(jsonPath("$.interestRate").value(0.5))
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

		InstallmentAccountRegisterRequest request = new InstallmentAccountRegisterRequest(memberId, accountProductId,
			password, withdrawalAccountNumber, payoutAccountNumber, monthlyInstallmentAmount, monthlyInstallmentDay,
			true);

		AccountRegisterResponse response = new AccountRegisterResponse(
			"Installment Product",
			withdrawalAccountNumber,
			payoutAccountNumber,
			"300001000001",
			0L,
			5.0,
			LocalDate.of(2027, 3, 25));

		Mockito.when(accountService.createInstallmentAccount(any(InstallmentAccountRegisterRequest.class)))
			.thenReturn(response);

		mockMvc.perform(post("/api/v1/accounts/installments").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.productName").value("Installment Product"))
			.andExpect(jsonPath("$.accountNumber").value("300001000001"))
			.andExpect(jsonPath("$.accountBalance").value(0))
			.andExpect(jsonPath("$.interestRate").value(5.0))
			.andExpect(jsonPath("$.accountExpiryDate").exists())
			.andDo(document("installment-account-register"));
	}

	@Test
	@DisplayName("사용자는 계좌의 잔액을 조회할 수 있다.")
	void can_get_account_balance() throws Exception {
		Long balance = 1000L;

		var request = new BalanceRequest("1101101234567890", "1234");

		var expect = new BalanceResponse("1101101234567890", "삼성은행", balance);

		given(accountService.getAccountBalance(any())).willReturn(expect);

		MvcResult result = mockMvc.perform(post("/api/v1/accounts/balance").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))).andDo(print()).andExpect(status().isOk()).andReturn();

		var response = objectMapper.readValue(result.getResponse().getContentAsString(),
			new TypeReference<BalanceResponse>() {
			});

		Assertions.assertThat(response).usingRecursiveComparison().isEqualTo(expect);
	}

	@Test
	@DisplayName("계좌를 삭제할 수 있다.")
	void can_delete_account() throws Exception {
		var request = new DeleteAccountRequest(
			"1101101234567890",
			"1234"
		);

		willDoNothing().given(accountService).deleteAccount(request);

		mockMvc.perform(patch("/api/v1/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());

		verify(accountService).deleteAccount(request);
	}

	@Test
	@DisplayName("계좌번호로 예금주 이름을 조회할 수 있다.")
	void can_find_account_owner_name() throws Exception {
		// given
		String fullAccountNumber = "100001123456";
		AccountOwnerNameRequest request = new AccountOwnerNameRequest(fullAccountNumber);

		// 마스킹된 이름 예시 (실제 마스킹 로직에 따라 달라질 수 있음)
		String maskedName = "홍*동";

		AccountOwnerNameResponse response = new AccountOwnerNameResponse(maskedName);

		given(accountService.getOwnerName(any(AccountOwnerNameRequest.class)))
				.willReturn(response);

		// when & then
		mockMvc.perform(post("/api/v1/accounts/owner")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.ownerName").value(maskedName))
				.andDo(document("get-account-owner-name"));
	}
}
