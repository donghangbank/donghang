package bank.donghang.core.account.application;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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
import bank.donghang.core.account.dto.request.AccountOwnerNameRequest;
import bank.donghang.core.account.dto.request.DemandAccountRegisterRequest;
import bank.donghang.core.account.dto.request.DepositAccountRegisterRequest;
import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.member.domain.Member;
import bank.donghang.core.member.domain.enums.MemberStatus;
import bank.donghang.core.member.domain.repository.MemberRepository;

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

	@Autowired
	private MemberRepository memberRepository;

	private Account testAccount;
	private AccountProduct demandProduct;
	private AccountProduct depositProduct;
	private AccountProduct installmentProduct;
	private Member testMember;

	@BeforeEach
	void setUp() {
		// Clear any existing data
		accountRepository.deleteAll();
		accountProductRepository.deleteAll();
		memberRepository.deleteAll();

		// 테스트 회원 생성 및 저장
		testMember = memberRepository.save(Member.of(
			"홍길동",
			"hong@example.com",
			"010-1234-5678",
			LocalDateTime.of(1990, 1, 1, 0, 0),
			"서울시 강남구",
			"12345",
			MemberStatus.ACTIVE
		));

		// Setup account products
		demandProduct = accountProductRepository.saveAccountProduct(
			AccountProduct.builder()
				.bankId(1L)
				.accountProductName("Demand Product 1")
				.accountProductDescription("Demand Product 1 for Test")
				.accountProductType(AccountProductType.DEMAND)
				.interestRate(0.5)
				.subscriptionPeriod(0)
				.build()
		);

		depositProduct = accountProductRepository.saveAccountProduct(
			AccountProduct.builder()
				.bankId(1L)
				.accountProductName("Deposit Product 1")
				.accountProductDescription("Deposit Product 1 for Test")
				.accountProductType(AccountProductType.DEPOSIT)
				.interestRate(2.5)
				.subscriptionPeriod(12)
				.minSubscriptionBalance(1L)
				.maxSubscriptionBalance(100_000_000L)
				.build()
		);

		installmentProduct = accountProductRepository.saveAccountProduct(
			AccountProduct.builder()
				.bankId(1L)
				.accountProductName("Installment Account 1")
				.accountProductDescription("Installment Account 1 for Test")
				.accountProductType(AccountProductType.INSTALLMENT)
				.interestRate(3.5)
				.subscriptionPeriod(24)
				.minSubscriptionBalance(1L)
				.maxSubscriptionBalance(10_000_000L)
				.build()
		);

		// 테스트 계좌 생성 및 저장
		testAccount = accountRepository.saveAccount(
			Account.builder()
				.memberId(testMember.getId()) // 저장된 회원의 ID 사용
				.accountProductId(demandProduct.getAccountProductId())
				.password("0000")
				.accountTypeCode("100")
				.branchCode("001")
				.accountNumber("12345678")
				.accountStatus(AccountStatus.ACTIVE)
				.dailyTransferLimit(1_000_000L)
				.singleTransferLimit(1_000_000L)
				.accountBalance(100_000L)
				.interestRate(1.0)
				.build()
		);
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
			String accountNumber = ((bank.donghang.core.account.dto.response.AccountSummaryResponse)summary)
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

	@Test
	@DisplayName("계좌 소유자명 조회 - 정상 케이스")
	void getOwnerName_success() {
		// given
		String fullAccountNumber = "10000112345678";
		AccountOwnerNameRequest request = new AccountOwnerNameRequest(fullAccountNumber);

		// when
		AccountOwnerNameResponse response = accountService.getOwnerName(request);

		// then
		assertNotNull(response);
		assertEquals("홍*동", response.ownerName()); // 마스킹 적용 확인
	}

	@Test
	@DisplayName("계좌 소유자명 마스킹 - 다양한 이름 길이 테스트")
	void getOwnerName_masking_variousNameLengths() {
		// given
		String fullAccountNumber = "10000112345678";
		AccountOwnerNameRequest request = new AccountOwnerNameRequest(fullAccountNumber);

		// 2글자 이름 테스트
		Member twoCharMember = memberRepository.save(createTestMember("이순"));
		Account twoCharAccount = accountRepository.saveAccount(
			createTestAccount(twoCharMember.getId(), "10000187654321")
		);
		assertEquals("이*", accountService.getOwnerName(
			new AccountOwnerNameRequest("10000187654321")).ownerName());

		// 3글자 이름 테스트 (기본 테스트 계정 사용)
		assertEquals("홍*동", accountService.getOwnerName(request).ownerName());

		// 4글자 이상 이름 테스트
		Member longNameMember = memberRepository.save(createTestMember("남궁토마스"));
		Account longNameAccount = accountRepository.saveAccount(
			createTestAccount(longNameMember.getId(), "10000111223344")
		);
		assertEquals("남***스", accountService.getOwnerName(
			new AccountOwnerNameRequest("10000111223344")).ownerName());
	}

	private Member createTestMember(String name) {
		return Member.of(
			name,
			"test@example.com",
			"010-0000-0000",
			LocalDateTime.now(),
			"주소",
			"12345",
			MemberStatus.ACTIVE
		);
	}

	private Account createTestAccount(Long memberId, String fullAccountNumber) {
		return Account.builder()
			.memberId(memberId)
			.accountProductId(demandProduct.getAccountProductId())
			.password("0000")
			.accountTypeCode(fullAccountNumber.substring(0, 3))
			.branchCode(fullAccountNumber.substring(3, 6))
			.accountNumber(fullAccountNumber.substring(6))
			.accountStatus(AccountStatus.ACTIVE)
			.dailyTransferLimit(1_000_000L)
			.singleTransferLimit(1_000_000L)
			.accountBalance(100_000L)
			.interestRate(1.0)
			.build();
	}
}
