package bank.donghang.donghang_api.cardcompany.application;

import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import bank.donghang.donghang_api.cardcompany.domain.repository.CardCompanyRepository;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardCompanyService {

    private final CardCompanyRepository cardCompanyRepository;

    @Transactional
    public Long createCardCompany(CardCompanyCreateRequest request) {

        CardCompany cardCompany = CardCompany.createCardCompany(
                request.name(),
                request.logoUrl()
        );

        return cardCompanyRepository.save(cardCompany).getId();
    }
}
