package bank.donghang.core.cardcompany.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bank.donghang.core.cardcompany.domain.CardCompany;
import bank.donghang.core.cardcompany.domain.repository.CardCompanyRepository;
import bank.donghang.core.cardcompany.dto.request.CardCompanyRequest;
import bank.donghang.core.cardcompany.dto.response.CardCompanySummaryResponse;
import bank.donghang.core.common.exception.BadRequestException;
import bank.donghang.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardCompanyService {

	private final CardCompanyRepository cardCompanyRepository;

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

	public List<CardCompanySummaryResponse> getAllCardCompanies() {

		List<CardCompanySummaryResponse> response = cardCompanyRepository.findAllCardCompanySummaries();

		return response;
	}

	@Transactional
	public void updateCardCompany(
		Long id,
		String newLogoUrl,
		CardCompanyRequest cardCompanyRequest
	) {

		checkCardCompanyExistence(id);

		CardCompany cardCompany = cardCompanyRepository.findById(id)
			.orElseThrow(() -> new BadRequestException(ErrorCode.CARD_COMPANY_NOT_FOUND));

		cardCompany.updateCardCompany(
			cardCompanyRequest.name(),
			newLogoUrl
		);
	}

	public void deleteCardCompany(Long id) {

		checkCardCompanyExistence(id);

		cardCompanyRepository.deleteCardCompany(id);
	}

	private void checkCardCompanyExistence(Long id) {
		if (!cardCompanyRepository.existsCardCompany(id)) {
			throw new BadRequestException(ErrorCode.CARD_COMPANY_NOT_FOUND);
		}
	}
}
