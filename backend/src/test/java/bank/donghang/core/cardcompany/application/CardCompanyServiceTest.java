package bank.donghang.core.cardcompany.application;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bank.donghang.core.cardcompany.domain.CardCompany;
import bank.donghang.core.cardcompany.domain.repository.CardCompanyRepository;
import bank.donghang.core.cardcompany.dto.request.CardCompanyRequest;

@ExtendWith(MockitoExtension.class)
public class CardCompanyServiceTest {

	@Mock
	CardCompanyRepository cardCompanyRepository;
	@InjectMocks
	private CardCompanyService cardCompanyService;

	@Test
	@DisplayName("카드사를 생성할 수 있다.")
	public void can_create_card_company() {

		var request = new CardCompanyRequest(
			"삼성카드"
		);

		String logoUrl = "www.donghang.com";

		CardCompany cardCompany = CardCompany.createCardCompany(
			request.name(),
			logoUrl
		);

		given(cardCompanyRepository.save(any(CardCompany.class)))
			.willReturn(cardCompany);

		cardCompanyService.createCardCompany(request, logoUrl);

		verify(cardCompanyRepository).save(any());
	}
}
