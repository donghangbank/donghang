package bank.donghang.core.accountproduct.domain.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.donghang.core.accountproduct.domain.QAccountProduct;
import bank.donghang.core.accountproduct.domain.enums.AccountProductType;
import bank.donghang.core.accountproduct.dto.response.AccountProductDetail;
import bank.donghang.core.accountproduct.dto.response.AccountProductSummary;
import bank.donghang.core.bank.domain.QBank;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountProductJpaRepositoryCustomImpl implements AccountProductJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<AccountProductSummary> getAccountProductsByAccountProductType(
		AccountProductType accountProductType,
		String pageToken,
		int pageSize
	) {
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
			.where(
				eqAccountProductType(accountProductType, accountProduct),
				isInRange(pageToken, accountProduct.accountProductId)
			)
			.orderBy(accountProduct.accountProductId.desc())
			.limit(pageSize + 1)
			.fetch();
	}

	private BooleanExpression eqAccountProductType(
		AccountProductType accountProductType,
		QAccountProduct accountProduct
	) {
		return accountProductType != null
			? accountProduct.accountProductType.eq(accountProductType)
			: null;
	}

	private BooleanExpression isInRange(String pageToken, NumberPath<Long> idPath) {
		if (pageToken == null || pageToken.isEmpty()) {
			return null;
		}
		try {
			Long lastId = Long.parseLong(pageToken);
			return idPath.loe(lastId);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public AccountProductDetail getAccountProductByName(String keyword) {
		QAccountProduct accountProduct = QAccountProduct.accountProduct;
		QBank bank = QBank.bank;

		return queryFactory.select(Projections.constructor(AccountProductDetail.class,
				accountProduct.accountProductId,
				accountProduct.accountProductName,
				accountProduct.accountProductDescription,
				accountProduct.bankId,
				bank.name,
				bank.logoUrl,
				accountProduct.interestRate,
				accountProduct.rateDescription,
				accountProduct.accountProductType.stringValue(),
				accountProduct.subscriptionPeriod,
				accountProduct.minSubscriptionBalance,
				accountProduct.maxSubscriptionBalance
			))
			.from(accountProduct)
			.join(bank)
			.on(accountProduct.bankId.eq(bank.id))
			.where(accountProduct.accountProductName.eq(keyword))
			.fetchOne();
	}

	public AccountProductDetail getAccountProductDetail(Long productId) {
		QAccountProduct accountProduct = QAccountProduct.accountProduct;
		QBank bank = QBank.bank;

		return queryFactory.select(Projections.constructor(AccountProductDetail.class,
				accountProduct.accountProductId,
				accountProduct.accountProductName,
				accountProduct.accountProductDescription,
				accountProduct.bankId,
				bank.name,
				bank.logoUrl,
				accountProduct.interestRate,
				accountProduct.rateDescription,
				accountProduct.accountProductType.stringValue(),
				accountProduct.subscriptionPeriod,
				accountProduct.minSubscriptionBalance,
				accountProduct.maxSubscriptionBalance
			))
			.from(accountProduct)
			.join(bank)
			.on(accountProduct.bankId.eq(bank.id))
			.where(accountProduct.accountProductId.eq(productId))
			.fetchOne();
	}
}
