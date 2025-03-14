package bank.donghang.donghang_api.bank.application;

import bank.donghang.donghang_api.bank.domain.Bank;
import bank.donghang.donghang_api.bank.domain.repository.BankRepository;
import bank.donghang.donghang_api.bank.dto.request.BankRequest;
import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;

    @Transactional
    public Long createBank(
            BankRequest request,
            String logoUrl
    ){

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
    ){

        checkBankExistence(id);

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.BANK_NOT_FOUND));

        bank = Bank.updateBank(
                request.name(),
                logoUrl
        );
    }

    @Transactional
    public void deleteBank(Long bankId) {
        bankRepository.delete(bankId);
    }

    private void checkBankExistence(Long id) {
        if (!bankRepository.exists(id)){
            throw new BadRequestException(ErrorCode.BANK_NOT_FOUND);
        }
    }
}
