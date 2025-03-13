package bank.donghang.donghang_api.cardcompany.application;

import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import bank.donghang.donghang_api.cardcompany.domain.repository.CardCompanyRepository;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyRequest;
import bank.donghang.donghang_api.cardcompany.dto.response.CardCompanySummaryResponse;
import bank.donghang.donghang_api.common.exception.BadRequestException;
import bank.donghang.donghang_api.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardCompanyService {

    private final CardCompanyRepository cardCompanyRepository;

    @Transactional
    public Long createCardCompany(
            CardCompanyRequest request,
            String logoUrl
    ) {

        CardCompany cardCompany = CardCompany.createCardCompany(
                request.name(),
                logoUrl
        );

        return cardCompanyRepository.save(cardCompany).getId();
    }

    public List<CardCompanySummaryResponse> findAllCardCompanies(){

        List<CardCompanySummaryResponse> response = cardCompanyRepository.findAllCardCompanySummaries();

        return response;
    }

    @Transactional
    public void updateCardCompany(
            Long id,
            String newLogoUrl,
            CardCompanyRequest cardCompanyRequest
    ){
        if (!cardCompanyRepository.existsCardCompany(id)){
            throw new BadRequestException(ErrorCode.CARD_COMPANY_NOT_FOUND);
        }

        CardCompany cardCompany = cardCompanyRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorCode.CARD_COMPANY_NOT_FOUND));

        cardCompany.updateCardCompany(
                cardCompanyRequest.name(),
                newLogoUrl
        );
    }

    @Transactional
    public void deleteCardCompany(Long id) {

        if (cardCompanyRepository.existsCardCompany(id)){
            throw new BadRequestException(ErrorCode.CARD_COMPANY_NOT_FOUND);
        }

        cardCompanyRepository.deleteCardCompany(id);
    }
}
