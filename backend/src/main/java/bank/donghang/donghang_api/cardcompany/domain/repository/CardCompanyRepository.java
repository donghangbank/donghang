package bank.donghang.donghang_api.cardcompany.domain.repository;

import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardCompanyRepository {

    private final CardCompanyJpaRepository cardCompanyJpaRepository;
    private final CardCompanyJpaRepositoryCustomImpl cardCompanyJpaRepositoryCustomImpl;

    public CardCompany save(CardCompany cardCompany) {
        return cardCompanyJpaRepository.save(cardCompany);
    }

    public List<CardCompanySummaryResponse> findAllCardCompanySummaries(){
        return cardCompanyJpaRepositoryCustomImpl.findAllCardCompanySummaries();
    }

    public void deleteCardCompany(Long id) {
        cardCompanyJpaRepository.deleteById(id);
    }
}
