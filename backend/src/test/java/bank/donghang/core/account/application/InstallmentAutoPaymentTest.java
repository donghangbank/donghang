// package bank.donghang.core.account.application;
//
// import java.time.LocalDate;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Random;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.util.StopWatch;
//
// import bank.donghang.core.account.domain.Account;
// import bank.donghang.core.account.domain.enums.AccountStatus;
// import bank.donghang.core.account.domain.repository.AccountRepository;
// import bank.donghang.core.account.dto.request.InstallmentAccountRegisterRequest;
// import bank.donghang.core.accountproduct.domain.AccountProduct;
// import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
// import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
//
// @ExtendWith(SpringExtension.class)
// @SpringBootTest
// @ActiveProfiles("test")
// @TestPropertySource(locations = "file:${user.dir}/test.env")
// @Transactional
// class InstallmentAutoPaymentTest {
//
// 	@Autowired
// 	private AccountService accountService;
//
// 	@Autowired
// 	private AccountProductRepository accountProductRepository;
//
// 	@Autowired
// 	private AccountRepository accountRepository;
//
// 	private final Random random = new Random();
// 	private final LocalDate today = LocalDate.now();
//
// 	private final Map<Integer, String> member2AccountNumber = new HashMap<>();
//
// 	private Long defaultDemandAccountProductId;
//
// 	@BeforeEach
// 	public void setUp() {
//
// 		for (int i = 0; i < 100; i++) {
// 			AccountProduct product = createTestAccountProduct(i);
// 			accountProductRepository.saveAccountProduct(product);
// 		}
//
// 		AccountProduct defaultDemandAccountProduct = AccountProduct.builder()
// 			.accountProductName("Default Demand Account")
// 			.accountProductDescription("Default Demand Account")
// 			.bankId(1L)
// 			.interestRate(0.5)
// 			.accountProductType(AccountProductType.DEMAND)
// 			.build();
//
// 		defaultDemandAccountProduct = accountProductRepository.saveAccountProduct(defaultDemandAccountProduct);
// 		defaultDemandAccountProductId = defaultDemandAccountProduct.getAccountProductId();
//
// 		// 2. 멤버 10,000명 및 자유 입출금 계좌 생성
// 		for (int i = 0; i < 10000; i++) {
// 			// 예시: 멤버 생성 및 자유 입출금 계좌 생성
// 			Long memberId = (long) i;
// 			Account freeAccount = createTestDemandAccount(memberId);
// 			accountRepository.saveAccount(freeAccount);
// 			String fullAccountNumber = freeAccount.getAccountTypeCode()
// 				+ freeAccount.getBranchCode()
// 				+ freeAccount.getAccountNumber();
//
// 			member2AccountNumber.put(i, fullAccountNumber);
// 		}
//
// 		// 3. 각 멤버가 100개 상품 중 하나를 랜덤 선택하여 가입
// 		List<AccountProduct> products = accountProductRepository.getAccountProductsByAccountProductType(AccountProductType.INSTALLMENT);
// 		for (int i = 0; i < 10000; i++) {
// 			Long memberId = (long) i;
// 			// 랜덤하게 하나의 상품 선택
// 			AccountProduct randomProduct = products.get(random.nextInt(products.size()));
//
// 			String withdrawalAccountNumber = member2AccountNumber.get(i);
// 			String payoutAccountNumber = member2AccountNumber.get(i);
//
// 			// 가입 요청 객체 생성
// 			InstallmentAccountRegisterRequest req = new InstallmentAccountRegisterRequest (
// 				memberId,
// 				randomProduct.getAccountProductId(),
// 				"0000",
// 				withdrawalAccountNumber,
// 				payoutAccountNumber,
// 				random.nextLong(100000L),
// 				today.getDayOfMonth(),
// 				false
// 			);
//
// 			// 적금 계좌 가입 (이때 InstallmentSchedule도 생성됨)
// 			accountService.createInstallmentAccount(req);
// 		}
// 	}
//
// 	@Test
// 	void testHandleInstallmentAccountSchedulePerformance() {
// 		LocalDate today = LocalDate.now();
// 		LocalDate dueDate = today.plusMonths(1).withDayOfMonth(today.getDayOfMonth());
//
// 		// 성능 측정을 위해 실행 전 시간 체크
// 		StopWatch sw = new StopWatch();
// 		sw.start("processInstallmentSchedule");
//
// 		// 4. 자동 이체 로직 실행 (10000개의 schedule에 대해 처리)
// 		accountService.handleInstallmentAccountSchedule(dueDate);
//
// 		sw.stop();
// 		System.out.println("Total processing time: " + sw.getTotalTimeMillis() + " ms");
//
// 		// 결과 검증: 예를 들어, 모든 schedule이 정상 처리되었는지 등
// 		// assertEquals(expectedCount, actualProcessedCount);
// 	}
//
// 	private AccountProduct createTestAccountProduct(int index) {
// 		return AccountProduct.builder()
// 			.accountProductName("Test Product " + index)
// 			.accountProductDescription("Description " + index)
// 			.bankId(1L)
// 			.interestRate(1.5)
// 			.accountProductType(AccountProductType.INSTALLMENT)
// 			.subscriptionPeriod(random.nextInt(1,60))
// 			.minSubscriptionBalance(10000L)
// 			.maxSubscriptionBalance(1000000L)
// 			.build();
// 	}
//
// 	private Account createTestDemandAccount(Long memberId) {
// 		// 자유 입출금 계좌 생성 시, 랜덤 초기 잔액 설정
// 		long randomBalance = 100000 + random.nextInt(100000);
// 		return Account.builder()
// 			.memberId(memberId)
// 			.accountProductId(defaultDemandAccountProductId)
// 			.accountTypeCode("100")
// 			.branchCode("001")
// 			.accountNumber(generateAccountNumberForMember(memberId))
// 			.password("0000")
// 			.accountStatus(AccountStatus.ACTIVE)
// 			.dailyTransferLimit(1000000L)
// 			.singleTransferLimit(500000L)
// 			.accountBalance(randomBalance)
// 			.monthlyInstallmentAmount(null)
// 			.monthlyInstallmentDay(null)
// 			.interestRate(0.0)
// 			.build();
// 	}
//
// 	private String generateAccountNumberForMember(Long memberId) {
// 		// 계좌 번호 생성 로직 (예시)
// 		return String.format("%06d", memberId);
// 	}
// }
