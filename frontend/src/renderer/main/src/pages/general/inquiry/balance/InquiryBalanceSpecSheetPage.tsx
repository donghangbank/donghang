import { balanceAPI } from "@renderer/api/inquiry";
import { InputContext } from "@renderer/contexts/InputContext";
import { formatAccountNumber, formatAmount } from "@renderer/utils/formatters";
import { useQuery } from "@tanstack/react-query";
import { useContext } from "react";
import { Link } from "react-router-dom";

export const InquiryBalanceSpecSheetPage = (): JSX.Element => {
	const { receivingAccountNumber, password } = useContext(InputContext);

	const { data, isError } = useQuery({
		queryKey: ["balance", receivingAccountNumber],
		queryFn: () => balanceAPI({ receivingAccountNumber, password }),
		enabled: !!receivingAccountNumber && !!password
	});

	if (!data || isError) {
		return <div>데이터를 불러오는 데 실패했습니다.</div>;
	}

	return (
		<div className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom">
			<span className="text-5xl font-bold text-center">잔액 확인</span>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">은행</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{data.bankName}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">계좌번호</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAccountNumber(String(data.accountNumber))}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">잔액</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAmount(String(data.balance))}</span>
				</div>
			</div>
			<div className="flex justify-center items-center">
				<button type="button" className="p-6 bg-blue rounded-xl w-full">
					<Link to={"/general/final"}>
						<span className="text-3xl text-white">확인</span>
					</Link>
				</button>
			</div>
		</div>
	);
};

export default InquiryBalanceSpecSheetPage;
