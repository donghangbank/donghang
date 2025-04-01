package bank.donghang.core.account.domain.repository;

import static bank.donghang.core.account.domain.QAccount.*;
import static bank.donghang.core.accountproduct.domain.QAccountProduct.*;
import static bank.donghang.core.bank.domain.QBank.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import bank.donghang.core.account.domain.QAccount;
import bank.donghang.core.account.dto.response.AccountOwnerNameResponse;
import bank.donghang.core.account.dto.response.AccountSummaryResponse;
import bank.donghang.core.account.dto.response.BalanceResponse;
import bank.donghang.core.accountproduct.domain.QAccountProduct;
import bank.donghang.core.bank.domain.QBank;
import bank.donghang.core.common.dto.PageInfo;
import bank.donghang.core.member.domain.QMember;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountJpaRepositoryCustomImpl implements AccountJpaRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	private static final int DEFAULT_PAGE_SIZE = 10;

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

	@Override
	public PageInfo<AccountSummaryResponse> getAccountSummaries(Long memberId, Long cursor) {
		QAccount account = QAccount.account;
		QAccountProduct accountProduct = QAccountProduct.accountProduct;
		QBank bank = QBank.bank;

		// 기본 조건: memberId 일치
		BooleanExpression predicate = account.memberId.eq(memberId);
		// 커서 보다 큰 row
		if (cursor != null) {
			predicate = predicate.and(account.accountId.goe(cursor));
		}

		// limit은 요청 사이즈 + 1(다음 페이지 존재여부 확인용)
		int pageSize = DEFAULT_PAGE_SIZE;
		List<Tuple> results = queryFactory
				.select(
						account.accountId,
						bank.name,
						account.accountTypeCode,
						account.branchCode,
						account.accountNumber,
						accountProduct.accountProductType,
						account.accountBalance
				)
				.from(account)
				.leftJoin(accountProduct).on(account.accountProductId.eq(accountProduct.accountProductId))
				.leftJoin(bank).on(accountProduct.bankId.eq(bank.id))
				.where(predicate)
				.orderBy(account.accountId.asc())
				.limit(pageSize + 1)
				.fetch();

		// 다음 페이지가 있는지 확인 (요청 건수보다 1개 더 조회됨)
		boolean hasNext = results.size() > pageSize;
		Long nextCursor = null;
		if (hasNext) {
			// 마지막 튜플은 다음 페이지를 위한 커서로 사용하고 리스트에서 제거
			Tuple last = results.remove(results.size() - 1);
			nextCursor = last.get(account.accountId);
		}

		// 조회 결과를 AccountSummaryResponse로 매핑
		List<AccountSummaryResponse> summaries = results.stream()
				.map(tuple -> new AccountSummaryResponse(
						tuple.get(bank.name),
						tuple.get(account.accountTypeCode)
								+ tuple.get(account.branchCode)
								+ tuple.get(account.accountNumber),
						tuple.get(accountProduct.accountProductType),
						tuple.get(account.accountBalance)
				))
				.collect(Collectors.toList());

		return PageInfo.of(
				nextCursor != null ? String.valueOf(nextCursor) : null,
				summaries,
				hasNext
		);
	}

	@Override
	public AccountOwnerNameResponse getAccountOwnerName(
			String accountTypeCode,
			String branchCode,
			String accountNumber
	) {
		QAccount account = QAccount.account;
		QMember member = QMember.member;
		String ownerName = queryFactory
				.select(member.name)
				.from(account)
				.join(member)
				.on(account.memberId.eq(member.id))
				.where(
						account.accountTypeCode.eq(accountTypeCode)
								.and(account.branchCode.eq(branchCode))
								.and(account.accountNumber.eq(accountNumber))
				)
				.fetchOne();

		return new AccountOwnerNameResponse(ownerName);
	}
}
