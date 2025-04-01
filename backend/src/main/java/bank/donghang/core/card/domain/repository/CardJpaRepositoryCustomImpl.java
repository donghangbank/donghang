package bank.donghang.core.card.domain.repository;

import bank.donghang.core.card.dto.response.CardPasswordResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static bank.donghang.core.account.domain.QAccount.account;
import static bank.donghang.core.card.domain.QCard.card;
import static bank.donghang.core.member.domain.QMember.member;

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
                        account.accountId,
                        member.name
                ))
                .from(card)
                .leftJoin(account)
                .on(card.accountId.eq(account.accountId))
                .leftJoin(member)
                .on(card.ownerId.eq(member.id))
                .where(card.cardNumber.eq(cardNumber))
                .fetchOne();
    }
}
