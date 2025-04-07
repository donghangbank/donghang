package bank.donghang.core.ledger.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;

import bank.donghang.core.common.controller.ControllerTest;
import bank.donghang.core.ledger.application.LedgerService;
import bank.donghang.core.ledger.domain.enums.ReconciliationCode;
import bank.donghang.core.ledger.dto.ErrorDetail;
import bank.donghang.core.ledger.dto.response.DailyReconciliationReport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LedgerController.class)
class LedgerControllerTest extends ControllerTest {

	@MockitoBean
	private LedgerService ledgerService;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("하루 동안의 거래를 검산하고 오류를 검출한다.")
	void get_daily_conciliation_report() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		List<ErrorDetail> errorDetails = List.of(
			ErrorDetail.forBalanceMismatch(1000000L, 950000L),
			new ErrorDetail(
				12345L,
				67890L,
				ReconciliationCode.AMOUNT_MISMATCH
			)
		);

		var expect = new DailyReconciliationReport(
			now,
			now.minusDays(1),
			100,
			95,
			5,
			2,
			1000000L,
			950000L,
			1950000L,
			50000L,
			errorDetails
		);

		when(ledgerService.getDailyReconciliationReport())
			.thenReturn(expect);

		MvcResult result = mockMvc.perform(get("/api/v1/ledgers/daily-reconciliation"))
			.andExpect(status().isOk())
			.andReturn();

		var response = objectMapper.readValue(
			result.getResponse().getContentAsString(),
			new TypeReference<DailyReconciliationReport>() {
			}
		);

		Assertions.assertThat(response).isEqualTo(expect);
	}
}