package bank.donghang.core.card.domain.repository;

import static bank.donghang.core.account.domain.QAccount.*;
import static bank.donghang.core.card.domain.QCard.*;
import static bank.donghang.core.member.domain.QMember.*;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.donghang.core.card.dto.response.CardPasswordResponse;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardJpaRepositoryCustomImpl implements CardJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public CardPasswordResponse checkCardPassword(String cardNumber) {
		return queryFactory.select(
				Projections.constructor(
					CardPasswordResponse.class,
					card.cardNumber,
					card.password,
					Expressions.stringTemplate(
						"concat({0}, concat({1}, {2}))",
						account.accountTypeCode,
						account.branchCode,
						account.accountNumber
					),
					member.name,
					member.id
				))
			.from(card)
			.leftJoin(account)
			.on(card.accountId.eq(account.accountId))
			.leftJoin(member)
			.on(card.ownerId.eq(member.id))
			.where(card.cardNumber.eq(cardNumber))
			.fetchOne();
	}

	@Override
	public Long findAccountIdByCardNumber(String cardNumber) {
		return queryFactory.select(card.accountId)
			.from(card)
			.where(card.cardNumber.eq(cardNumber))
			.fetchOne();
	}

}
