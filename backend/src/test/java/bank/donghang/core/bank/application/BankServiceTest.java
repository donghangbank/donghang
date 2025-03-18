package bank.donghang.core.bank.application;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.bank.domain.Bank;
import bank.donghang.core.bank.domain.repository.BankRepository;
import bank.donghang.core.bank.dto.request.BankRequest;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class BankServiceTest {

	@InjectMocks
	private BankService bankService;

	@Mock
	private BankRepository bankRepository;

	@Test
	@DisplayName("은행을 생성할 수 있다.")
	public void can_create_bank() {

		var request = new BankRequest(
			"삼성은행"
		);

		String logoUrl = "www.donghang.com";

		Bank bank = Bank.createBank(
			request.name(),
			logoUrl
		);
		given(bankRepository.save(any(Bank.class)))
			.willReturn(bank);

		bankService.createBank(request, logoUrl);

		verify(bankRepository).save(any());
	}

	@Test
	@DisplayName("모든 은행을 조회할 수 있다.")
	public void can_find_all_banks() {
		List<Bank> banks = List.of(
			Bank.createBank(
				"종하은행",
				"www.donghang.com"
			)
		);
		given(bankRepository.findAll()).willReturn(banks);

		List<Bank> result = bankService.getAllBanks();

		Assertions.assertThat(result).hasSize(1);
		Assertions.assertThat(result.get(0).getName()).isEqualTo("종하은행");
		verify(bankRepository).findAll();
	}

	@Test
	@DisplayName("존재하지 않는 은행을 수정하려 하면 예외가 발생한다.")
	public void update_non_existing_bank_throws_exception() {

		var request = new BankRequest(
			"삼성은행"
		);

		Long bankId = 1L;
		given(bankRepository.exists(eq(bankId))).willReturn(false);

		Assertions.assertThatThrownBy(() -> bankService.updateBank(request, "www.new-logo.com", bankId))
			.isInstanceOf(BadRequestException.class)
			.hasMessage(ErrorCode.BANK_NOT_FOUND.getMessage());

		verify(bankRepository, never()).findById(any());
	}

	@Test
	@DisplayName("은행을 삭제할 수 있다.")
	public void can_delete_bank() {

		Long bankId = 1L;

		bankService.deleteBank(bankId);

		verify(bankRepository).delete(eq(bankId));
	}
}
