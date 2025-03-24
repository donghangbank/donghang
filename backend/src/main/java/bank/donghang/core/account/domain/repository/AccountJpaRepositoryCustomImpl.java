package bank.donghang.core.account.domain.repository;

import static bank.donghang.core.account.domain.QAccount.*;
import static bank.donghang.core.accountproduct.domain.QAccountProduct.*;
import static bank.donghang.core.bank.domain.QBank.*;

import org.springframework.stereotype.Repository;

import bank.donghang.core.account.dto.response.BalanceResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountJpaRepositoryCustomImpl implements AccountJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public BalanceResponse getAccountBalance(
		String accountTypeCode,
		String branchCode,
		String accountNumber
	) {
		return queryFactory.select(
				Projections.constructor(
					BalanceResponse.class,
					Expressions.stringTemplate(
						"concat({0}, concat({1}, {2}))",
						account.accountTypeCode,
						account.branchCode,
						account.accountNumber
					),
					bank.name,
					account.accountBalance
				))
			.from(account)
			.leftJoin(accountProduct)
			.on(account.accountProductId.eq(accountProduct.accountProductId))
			.leftJoin(bank)
			.on(accountProduct.bankId.eq(bank.id))
			.where(
				account.accountTypeCode.eq(accountTypeCode)
					.and(account.branchCode.eq(branchCode))
					.and(account.accountNumber.eq(accountNumber))
			)
			.fetchOne();
	}
}
