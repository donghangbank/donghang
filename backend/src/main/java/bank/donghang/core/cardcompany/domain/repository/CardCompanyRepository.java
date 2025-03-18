package bank.donghang.core.cardcompany.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import bank.donghang.core.cardcompany.domain.CardCompany;
import bank.donghang.core.cardcompany.dto.response.CardCompanySummaryResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardCompanyRepository {

	private final CardCompanyJpaRepository cardCompanyJpaRepository;
	private final CardCompanyJpaRepositoryCustomImpl cardCompanyJpaRepositoryCustomImpl;

	public CardCompany save(CardCompany cardCompany) {
		return cardCompanyJpaRepository.save(cardCompany);
	}

	public Optional<CardCompany> findById(Long id) {
		return cardCompanyJpaRepository.findById(id);
	}

	public List<CardCompanySummaryResponse> findAllCardCompanySummaries() {
		return cardCompanyJpaRepositoryCustomImpl.findAllCardCompanySummaries();
	}

	public void deleteCardCompany(Long id) {
		cardCompanyJpaRepository.deleteById(id);
	}

	public boolean existsCardCompany(Long id) {
		return cardCompanyJpaRepository.existsById(id);
	}
}
