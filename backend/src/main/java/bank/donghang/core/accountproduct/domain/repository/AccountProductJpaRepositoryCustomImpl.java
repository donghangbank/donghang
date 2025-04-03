package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.donghang.core.accountproduct.domain.QAccountProduct;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountProductJpaRepositoryCustomImpl implements AccountProductJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<AccountProductSummary> getAccountProductsByQueryDsl() {
		QAccountProduct accountProduct = QAccountProduct.accountProduct;

		return queryFactory
			.select(Projections.constructor(AccountProductSummary.class,
				accountProduct.accountProductId,
				accountProduct.accountProductName,
				accountProduct.bankId,
				accountProduct.interestRate,
				accountProduct.subscriptionPeriod,
				accountProduct.minSubscriptionBalance,
				accountProduct.maxSubscriptionBalance,
				accountProduct.accountProductType.stringValue()
			))
			.from(accountProduct)
			.fetch();
	}
}
