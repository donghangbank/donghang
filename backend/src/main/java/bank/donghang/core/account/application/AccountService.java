package bank.donghang.core.account.application;

import org.springframework.stereotype.Service;

import bank.donghang.core.account.domain.Account;
import bank.donghang.core.account.domain.repository.AccountRepository;
import bank.donghang.core.account.dto.request.AccountRegisterRequest;
import bank.donghang.core.account.dto.response.AccountRegisterResponse;
import bank.donghang.core.accountproduct.domain.AccountProduct;
import bank.donghang.core.accountproduct.domain.repository.AccountProductRepository;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final AccountRepository accountRepository;
	private final AccountProductRepository accountProductRepository;

	@Transactional
	public AccountRegisterResponse createAccount(AccountRegisterRequest accountRegisterRequest) {
		// 1. 상품 확인
		Long productId = accountRegisterRequest.accountProductId();
		AccountProduct accountProduct = accountProductRepository.getAccountProductById(productId)
			.orElseThrow(() -> new BadRequestException(ErrorCode.PRODUCT_NOT_FOUND));

		// 2. 다음 계좌번호 생성
		// todo. 은행, 상품 별 accountTypeCode 매핑
		String nextAccountNumber = accountRepository.getNextAccountNumber(
			"100",
			"001"
		);

		// 3. 엔티티 생성
		Account account = accountRegisterRequest.toEntity(
			nextAccountNumber,
			accountProduct.getInterestRate()
		);

		// 4. DB에 저장
		Account savedAccount = accountRepository.saveAccount(account);

		// 5. DTO로 변환해 응답
		return AccountRegisterResponse.from(savedAccount, accountProduct);
	}
}
