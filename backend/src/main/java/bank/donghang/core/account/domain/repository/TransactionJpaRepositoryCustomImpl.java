package bank.donghang.core.account.domain.repository;

import static bank.donghang.core.account.domain.QAccount.*;
import static bank.donghang.core.account.domain.QTransaction.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import bank.donghang.core.account.dto.response.TransactionHistoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionJpaRepositoryCustomImpl implements TransactionJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<TransactionHistoryResponse> getTransactionHistoriesByFullAccountNumber(
		String accountTypeCode,
		String branchCode,
		String accountNumber,
		String pageToken,
		int pageSize
	) {
		return queryFactory.select(
				Projections.constructor(
					TransactionHistoryResponse.class,
					transaction.id,
					transaction.createdAt,
					transaction.type,
					transaction.description,
					transaction.amount,
					transaction.balance
				))
			.from(transaction)
			.leftJoin(account)
			.on(transaction.accountId.eq(account.accountId))
			.where(
				account.accountTypeCode.eq(accountTypeCode)
					.and(account.branchCode.eq(branchCode))
					.and(account.accountNumber.eq(accountNumber)),
				isInRange(pageToken)
			)
			.groupBy(transaction.id)
			.orderBy(transaction.id.desc())
			.limit(pageSize + 1)
			.fetch();
	}

	private BooleanExpression isInRange(String pageToken) {
		if (pageToken == null) {
			return null;
		}

		return transaction.id.lt(Long.parseLong(pageToken));
	}
}
