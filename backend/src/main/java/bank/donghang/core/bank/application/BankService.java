package bank.donghang.core.bank.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bank.donghang.core.bank.domain.Bank;
import bank.donghang.core.bank.domain.repository.BankRepository;
import bank.donghang.core.bank.dto.request.BankRequest;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankService {

	private final BankRepository bankRepository;

	public Long createBank(
		BankRequest request,
		String logoUrl
	) {

		Bank bank = Bank.createBank(
			request.name(),
			logoUrl
		);

		return bankRepository.save(bank).getId();
	}

	public List<Bank> getAllBanks() {
		return bankRepository.findAll();
	}

	@Transactional
	public void updateBank(
		BankRequest request,
		String logoUrl,
		Long id
	) {

		checkBankExistence(id);

		Bank bank = bankRepository.findById(id)
			.orElseThrow(() -> new BadRequestException(ErrorCode.BANK_NOT_FOUND));

		bank = Bank.updateBank(
			request.name(),
			logoUrl
		);
	}

	public void deleteBank(Long bankId) {
		bankRepository.delete(bankId);
	}

	private void checkBankExistence(Long id) {
		if (!bankRepository.exists(id)) {
			throw new BadRequestException(ErrorCode.BANK_NOT_FOUND);
		}
	}
}
