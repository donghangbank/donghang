package bank.donghang.donghang_api.cardcompany.application;

import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import bank.donghang.donghang_api.cardcompany.domain.repository.CardCompanyJpaRepository;
import bank.donghang.donghang_api.cardcompany.domain.repository.CardCompanyRepository;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyCreateRequest;
import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CardCompanySummaryResponse> findAllCardCompanies(){

        List<CardCompanySummaryResponse> response = cardCompanyRepository.findAllCardCompanySummaries();

        return response;
    }

    @Transactional
    public void deleteCardCompany(Long id) {

        cardCompanyRepository.deleteCardCompany(id);
    }
}
