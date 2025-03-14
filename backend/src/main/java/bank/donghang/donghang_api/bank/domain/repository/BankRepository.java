package bank.donghang.donghang_api.bank.domain.repository;

import bank.donghang.donghang_api.bank.domain.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BankRepository {

    private final BankJpaRepository bankJpaRepository;

    public Bank save(Bank bank) {
        return bankJpaRepository.save(bank);
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
}
