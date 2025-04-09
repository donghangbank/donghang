import { balanceAPI } from "@renderer/api/inquiry";
import { InputContext } from "@renderer/contexts/InputContext";
import { formatAccountNumber, formatAmount } from "@renderer/utils/formatters";
import { useQuery } from "@tanstack/react-query";
import { useContext } from "react";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

export const InquiryBalanceSpecSheetPage = (): JSX.Element => {
	const { receivingAccountNumber, password } = useContext(InputContext);

	const { data, isError, isSuccess } = useQuery({
		queryKey: ["balance", receivingAccountNumber],
		queryFn: () => balanceAPI({ receivingAccountNumber, password }),
		enabled: !!receivingAccountNumber && !!password
	});

	if (!data || isError) {
		return <div></div>;
	}

	return (
		<motion.div
			initial={{ opacity: 0, y: 20 }}
			animate={isSuccess ? { opacity: 1, y: 0 } : { opacity: 0 }}
			transition={{ duration: 0.5 }}
			className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom"
		>
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
		</motion.div>
	);
};

export default InquiryBalanceSpecSheetPage;
