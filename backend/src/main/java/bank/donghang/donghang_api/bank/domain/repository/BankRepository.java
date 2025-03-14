package bank.donghang.donghang_api.bank.domain.repository;

import bank.donghang.donghang_api.bank.domain.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
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

    public void delete(Bank bank) {
        bankJpaRepository.delete(bank);
    }

    public boolean exists(Long id) {
        return bankJpaRepository.existsById(id);
    }
}
