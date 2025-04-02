import { InputContext } from "@renderer/contexts/InputContext";
import { formatAccountNumber, formatAmount } from "@renderer/utils/formatters";
import { useContext } from "react";
import { Link } from "react-router-dom";

export const TransferInfoSpecSheetPage = (): JSX.Element => {
	const { account, amount } = useContext(InputContext);

	return (
		<div className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom">
			<span className="text-5xl font-bold text-center">거래 명세표</span>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">거래 일시</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[350px]">
					<span>2025-04-01 13:00</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">수취인</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[350px]">
					<span>이재백</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">수취계좌</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[350px]">
					<span>{formatAccountNumber(account)}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">송금인</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[350px]">
					<span>이주현</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">금액</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[350px]">
					<span>{formatAmount(amount)}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">잔액</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[350px]">
					<span>{formatAmount("167000")}</span>
				</div>
			</div>
			<div className="flex justify-center items-center">
				<button type="button" className="p-6 bg-blue rounded-xl w-full">
					<Link to={"/general/final"}>
						<span className="text-3xl text-white">확인</span>{" "}
					</Link>
				</button>
			</div>
		</div>
	);
};

export default TransferInfoSpecSheetPage;
