import { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { InputContext } from "@renderer/contexts/InputContext";
import { historyAPI, historyAPIResponse } from "@renderer/api/inquiry";
import {
	formatAccountNumber,
	formatAmount,
	formatTransactionTime
} from "@renderer/utils/formatters";
import { Transaction } from "@renderer/types/transaction";

export const InquiryHistorySpecSheetPage = (): JSX.Element => {
	const { receivingAccountNumber, password } = useContext(InputContext);
	const [allTransactions, setAllTransactions] = useState<Transaction[]>([]);
	const [currentPage, setCurrentPage] = useState(0);
	const [pageTokens, setPageTokens] = useState<(number | null)[]>([null]);
	const [hasNext, setHasNext] = useState(false);

	const { data, isError, isFetching, isSuccess } = useQuery<historyAPIResponse, Error>({
		queryKey: ["history", receivingAccountNumber, pageTokens[currentPage]],
		queryFn: () =>
			historyAPI({
				receivingAccountNumber,
				password,
				pageToken: pageTokens[currentPage] ?? undefined
			}),
		enabled: !!receivingAccountNumber && !!password
	});

	useEffect(() => {
		if (isSuccess && data) {
			if (currentPage === pageTokens.length - 1) {
				setAllTransactions((prev) => [...prev, ...data.data]);
				setPageTokens((prev) => [...prev, data.pageToken]);
				setHasNext(data.hasNext);
			}
		}
	}, [isSuccess, data, currentPage, pageTokens]);

	const handleNext = (): void => {
		if ((currentPage + 1) * 3 >= allTransactions.length && hasNext) {
			setCurrentPage((prev) => prev + 1);
		} else {
			setCurrentPage((prev) => prev + 1);
		}
	};

	const handlePrevious = (): void => {
		setCurrentPage((prev) => Math.max(prev - 1, 0));
	};

	const visibleTransactions = allTransactions.slice(currentPage * 3, (currentPage + 1) * 3);

	if (isError) {
		return <div>데이터를 불러오는 데 실패했습니다</div>;
	}

	if (!data) {
		return <div>거래내역이 존재하지 않습니다</div>;
	}

	return (
		<div className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom">
			<span className="text-5xl font-bold text-center">거래내역조회</span>

			<div className="flex justify-between gap-20 items-center">
				<span className="text-black text-3xl font-bold">계좌번호</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px] shadow-custom">
					<span>{formatAccountNumber(receivingAccountNumber)}</span>
				</div>
			</div>

			<div className="flex flex-col gap-4 max-h-[500px]">
				{visibleTransactions.map((transaction) => (
					<div
						key={transaction.transactionId}
						className="flex justify-between items-center p-6 bg-cloudyBlue rounded-3xl shadow-custom"
					>
						<div className="flex flex-col gap-2">
							<span className="text-xl font-semibold text-gray-800">
								{formatTransactionTime(transaction.transactionTime)}
							</span>
							<span className="text-lg text-gray-600">{transaction.description}</span>
							<span
								className={`text-2xl font-bold ${
									transaction.type === "DEPOSIT" ? "text-red" : "text-blue"
								}`}
							>
								{transaction.type === "DEPOSIT" ? "입금" : "출금"}
							</span>
						</div>
						<div className="flex flex-col items-end gap-2">
							<span
								className={`text-3xl font-bold ${
									transaction.type === "DEPOSIT" ? "text-red" : "text-blue"
								}`}
							>
								{formatAmount(String(transaction.amount))}
							</span>
							<span className="text-xl font-bold">
								잔액 {formatAmount(String(transaction.balance))}
							</span>
						</div>
					</div>
				))}
			</div>

			<div className="grid grid-cols-3 justify-center items-center gap-4">
				<button
					onClick={handlePrevious}
					disabled={currentPage === 0 || isFetching}
					className="p-6 bg-gray-900 rounded-xl disabled:bg-gray-400 disabled:cursor-not-allowed flex-1"
				>
					<span className="text-3xl text-white">이전</span>
				</button>
				<button
					onClick={handleNext}
					disabled={(!hasNext && (currentPage + 1) * 4 >= allTransactions.length) || isFetching}
					className="p-6 bg-gray-900 rounded-xl disabled:bg-gray-400 disabled:cursor-not-allowed flex-1"
				>
					<span className="text-3xl text-white">{isFetching ? "로딩 중..." : "다음"}</span>
				</button>
				<Link to={"/general/final"}>
					<button type="button" className="p-6 bg-blue rounded-xl w-full">
						<span className="text-3xl text-white">확인</span>
					</button>
				</Link>
			</div>
		</div>
	);
};

export default InquiryHistorySpecSheetPage;
