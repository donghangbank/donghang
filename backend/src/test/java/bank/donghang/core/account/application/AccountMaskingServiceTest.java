package bank.donghang.core.account.application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.enums.AccountStatus;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.common.dto.PageInfo;

@ActiveProfiles("test")
@TestPropertySource(locations = "file:${user.dir}/test.env")
@SpringBootTest
class AccountMaskingServiceTest {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountProductRepository accountProductRepository;

	private AccountProduct demandProduct;
	private AccountProduct depositProduct;
	private AccountProduct installmentProduct;

	@BeforeEach
	void setUp() {
		// Clear any existing data
		accountRepository.deleteAll();
		accountProductRepository.deleteAll();

		// Setup account products without manually assigning IDs
		AccountProduct tempDemand = AccountProduct.builder()
			.bankId(1L)
			.accountProductName("Demand Product 1")
			.accountProductDescription("Demand Product 1 for Test")
			.accountProductType(AccountProductType.DEMAND)
			.interestRate(0.5)
			.subscriptionPeriod(0L)
			.build();

		AccountProduct tempDeposit = AccountProduct.builder()
			.bankId(1L)
			.accountProductName("Deposit Product 1")
			.accountProductDescription("Deposit Product 1 for Test")
			.accountProductType(AccountProductType.DEPOSIT)
			.interestRate(2.5)
			.subscriptionPeriod(12L)
			.minSubscriptionBalance(1L)
			.maxSubscriptionBalance(100_000_000L)
			.build();

		AccountProduct tempInstallment = AccountProduct.builder()
			.bankId(1L)
			.accountProductName("Installment Account 1")
			.accountProductDescription("Installment Account 1 for Test")
			.accountProductType(AccountProductType.INSTALLMENT)
			.interestRate(3.5)
			.subscriptionPeriod(24L)
			.minSubscriptionBalance(1L)
			.maxSubscriptionBalance(10_000_000L)
			.build();

		// Save products and capture generated IDs
		demandProduct = accountProductRepository.saveAccountProduct(tempDemand);
		depositProduct = accountProductRepository.saveAccountProduct(tempDeposit);
		installmentProduct = accountProductRepository.saveAccountProduct(tempInstallment);
	}

	@Test
	@DisplayName("사용자는 본인의 계좌 목록을 조회할 수 있다. (Masking ON)")
	void getMyAccounts_whenMaskingEnabled() {
		Long memberId = 1L;
		String pageToken = null; // 첫 페이지
		PageInfo<?> result = accountService.getMyAccounts(
			new bank.donghang.core.account.dto.request.MyAccountsRequest(memberId),
			pageToken);
		assertNotNull(result);
		// 조회된 계좌 목록이 있다면 각 계좌의 accountNumber에 마스킹 처리가 되어있는지 확인
		result.data().forEach(summary -> {
			String accountNumber = ((bank.donghang.core.account.dto.response.AccountSummaryResponse) summary)
				.accountNumber();
			if (accountNumber != null) {
				assertTrue(isMasked(accountNumber));
			}
		});
	}

	@Test
	void createDemandAccount_shouldReturnMaskedAccountNumbers_whenMaskingEnabled() {
		// Use the generated product id for demandProduct
		DemandAccountRegisterRequest request = new DemandAccountRegisterRequest(
			1L, demandProduct.getAccountProductId(), "0000", false
		);

		AccountRegisterResponse response = accountService.createDemandAccount(request);

		assertNotNull(response);
		// Masking should occur when disableMasking is false
		assertTrue(response.withdrawalAccountNumber() == null || isMasked(response.withdrawalAccountNumber()));
		assertTrue(response.payoutAccountNumber() == null || isMasked(response.payoutAccountNumber()));
	}

	@Test
	void createDepositAccount_shouldReturnMaskedAccountNumbers_whenMaskingEnabled() {
		// Create withdrawal and payout accounts with sufficient balance
		Account withdrawalAccount = Account.builder()
			.memberId(1L)
			.accountProductId(demandProduct.getAccountProductId())
			.password("0000")
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber("123456")  // full: "100001123456"
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100_000_000L)
			.singleTransferLimit(10_000_000L)
			.accountBalance(1_000_000L) // Sufficient balance for transfer
			.interestRate(0.5)
			.build();
		accountRepository.saveAccount(withdrawalAccount);

		Account payoutAccount = Account.builder()
			.memberId(1L)
			.accountProductId(demandProduct.getAccountProductId())
			.password("0000")
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber("654321")  // full: "100001654321"
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100_000_000L)
			.singleTransferLimit(10_000_000L)
			.accountBalance(500_000L)
			.interestRate(0.5)
			.build();
		accountRepository.saveAccount(payoutAccount);

		String withdrawalFullAccount = "100001123456";
		String payoutFullAccount = "100001654321";

		DepositAccountRegisterRequest request = new DepositAccountRegisterRequest(
			1L, depositProduct.getAccountProductId(), "0000",
			withdrawalFullAccount, payoutFullAccount, 100000L, false
		);

		AccountRegisterResponse response = accountService.createDepositAccount(request);

		assertNotNull(response);
		assertTrue(isMasked(response.withdrawalAccountNumber()));
		assertTrue(isMasked(response.payoutAccountNumber()));
	}

	@Test
	void createInstallmentAccount_shouldReturnMaskedAccountNumbers_whenMaskingEnabled() {
		// Create withdrawal and payout accounts with sufficient balance
		Account withdrawalAccount = Account.builder()
			.memberId(1L)
			.accountProductId(demandProduct.getAccountProductId())
			.password("0000")
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber("123456")  // full: "100001123456"
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100_000_000L)
			.singleTransferLimit(10_000_000L)
			.accountBalance(1_000_000L) // Sufficient balance
			.interestRate(0.5)
			.build();
		accountRepository.saveAccount(withdrawalAccount);

		Account payoutAccount = Account.builder()
			.memberId(1L)
			.accountProductId(demandProduct.getAccountProductId())
			.password("0000")
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber("654321")  // full: "100001654321"
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100_000_000L)
			.singleTransferLimit(10_000_000L)
			.accountBalance(500_000L)
			.interestRate(0.5)
			.build();
		accountRepository.saveAccount(payoutAccount);

		String withdrawalFullAccount = "100001123456";
		String payoutFullAccount = "100001654321";

		InstallmentAccountRegisterRequest request = new InstallmentAccountRegisterRequest(
			1L, installmentProduct.getAccountProductId(), "0000",
			withdrawalFullAccount, payoutFullAccount, 50000L, 15, false
		);

		AccountRegisterResponse response = accountService.createInstallmentAccount(request);

		assertNotNull(response);
		assertTrue(isMasked(response.withdrawalAccountNumber()));
		assertTrue(isMasked(response.payoutAccountNumber()));
	}

	@Test
	void createAccount_shouldReturnUnmaskedAccountNumbers_whenMaskingDisabled() {
		// Create withdrawal and payout accounts with sufficient balance
		Account withdrawalAccount = Account.builder()
			.memberId(1L)
			.accountProductId(demandProduct.getAccountProductId())
			.password("0000")
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber("123456")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100_000_000L)
			.singleTransferLimit(10_000_000L)
			.accountBalance(1_000_000L) // Sufficient balance
			.interestRate(0.5)
			.build();
		accountRepository.saveAccount(withdrawalAccount);

		Account payoutAccount = Account.builder()
			.memberId(1L)
			.accountProductId(demandProduct.getAccountProductId())
			.password("0000")
			.accountTypeCode("100")
			.branchCode("001")
			.accountNumber("654321")
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(100_000_000L)
			.singleTransferLimit(10_000_000L)
			.accountBalance(500_000L)
			.interestRate(0.5)
			.build();
		accountRepository.saveAccount(payoutAccount);

		String withdrawalFullAccount = "100001123456";
		String payoutFullAccount = "100001654321";

		DepositAccountRegisterRequest request = new DepositAccountRegisterRequest(
			1L, depositProduct.getAccountProductId(), "0000",
			withdrawalFullAccount, payoutFullAccount, 100000L, true
		);

		AccountRegisterResponse response = accountService.createDepositAccount(request);

		assertNotNull(response);
		// When masking is disabled, the original account numbers are returned
		assertEquals(withdrawalFullAccount, response.withdrawalAccountNumber());
		assertEquals(payoutFullAccount, response.payoutAccountNumber());
	}

	private boolean isMasked(String accountNumber) {
		return accountNumber != null && accountNumber.contains("*****");
	}
}
