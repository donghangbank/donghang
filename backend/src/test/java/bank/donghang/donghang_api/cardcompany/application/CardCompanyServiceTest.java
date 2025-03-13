package bank.donghang.donghang_api.cardcompany.application;

import bank.donghang.donghang_api.cardcompany.domain.CardCompany;
import bank.donghang.donghang_api.cardcompany.domain.repository.CardCompanyRepository;
import bank.donghang.donghang_api.cardcompany.dto.request.CardCompanyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CardCompanyServiceTest {

    @InjectMocks
    private CardCompanyService cardCompanyService;

    @Mock
    CardCompanyRepository cardCompanyRepository;

    @Test
    @DisplayName("카드사를 생성할 수 있다.")
    public void can_create_card_company(){

        var request = new CardCompanyRequest(
                "삼성카드",
                "www.test.com"
        );

        CardCompany cardCompany = CardCompany.createCardCompany(
                request.name(),
                request.logoUrl()
        );

        given(cardCompanyRepository.save(any(CardCompany.class)))
                .willReturn(cardCompany);

        cardCompanyService.createCardCompany(request);

        verify(cardCompanyRepository).save(any());
    }
}
