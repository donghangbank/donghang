package bank.donghang.core.bank.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bank.donghang.core.bank.domain.Bank;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankRepository {

	private final BankJpaRepository bankJpaRepository;

	public Bank save(Bank bank) {
		return bankJpaRepository.save(bank);
	}

	public Optional<Bank> findById(Long id) {
		return bankJpaRepository.findById(id);
	}

	public List<Bank> findAll() {
		return bankJpaRepository.findAll();
	}

	public void update(Bank bank) {
		bankJpaRepository.save(bank);
	}

	public void delete(Long id) {
		bankJpaRepository.deleteById(id);
	}

	public boolean exists(Long id) {
		return bankJpaRepository.existsById(id);
	}
}
