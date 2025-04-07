package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.donghang.core.accountproduct.domain.QAccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.bank.domain.QBank;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountProductJpaRepositoryCustomImpl implements AccountProductJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<AccountProductSummary> getAccountProductsByQueryDsl(AccountProductType accountProductType) {
		QAccountProduct accountProduct = QAccountProduct.accountProduct;
		QBank bank = QBank.bank;

		return queryFactory
			.select(Projections.constructor(AccountProductSummary.class,
				accountProduct.accountProductId,
				accountProduct.accountProductName,
				accountProduct.bankId,
				bank.name,
				bank.logoUrl,
				accountProduct.interestRate,
				accountProduct.subscriptionPeriod,
				accountProduct.minSubscriptionBalance,
				accountProduct.maxSubscriptionBalance,
				accountProduct.accountProductType.stringValue()
			))
			.from(accountProduct)
			.join(bank)
			.on(accountProduct.bankId.eq(bank.id))
			.where(eqAccountProductType(accountProductType, accountProduct))
			.fetch();
	}

	private BooleanExpression eqAccountProductType(
		AccountProductType accountProductType,
		QAccountProduct accountProduct
	) {
		if (accountProductType == null) {
			return null;
		}
		return accountProduct.accountProductType.eq(accountProductType);
	}
}
