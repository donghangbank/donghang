import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import {
	formatAccountNumber,
	formatAmount,
	formatTransactionTime
} from "@renderer/utils/formatters";
import { useContext } from "react";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

export const TransferInfoSpecSheetPage = (): JSX.Element => {
	const { amount, receivingAccountNumber, recipientName, sendingAccountBalance, transactionTime } =
		useContext(SpecSheetContext);

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom"
		>
			<span className="text-5xl font-bold text-center">거래 명세표</span>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-2xl font-bold">거래 일시</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatTransactionTime(transactionTime)}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">수취인</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{recipientName}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">수취계좌</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAccountNumber(receivingAccountNumber)}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">금액</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAmount(String(amount))}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">잔액</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAmount(String(sendingAccountBalance))}</span>
				</div>
			</div>
			<div className="flex justify-center items-center">
				<button type="button" className="p-6 bg-blue rounded-xl w-full">
					<Link to={"/general/final"}>
						<span className="text-3xl text-white">확인</span>{" "}
					</Link>
				</button>
			</div>
		</motion.div>
	);
};

export default TransferInfoSpecSheetPage;
