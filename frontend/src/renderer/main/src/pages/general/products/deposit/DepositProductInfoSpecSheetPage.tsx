import { ProductContext } from "@renderer/contexts/ProductContext";
import { formatAccountNumber, formatAmount } from "@renderer/utils/formatters";
import { useContext } from "react";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";

export const DepositProductInfoSpecSheetPage = (): JSX.Element => {
	const { productName, accountBalance, accountNumber, interestRate, accountExpiryDate } =
		useContext(ProductContext);

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom"
		>
			<span className="text-5xl font-bold text-center">가입을 완료했어요</span>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">상품명</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{productName}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">예치금</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAmount(String(accountBalance))}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">계좌번호</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAccountNumber(accountNumber)}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">만기일</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{accountExpiryDate}</span>
				</div>
			</div>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">연 금리</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{interestRate} %</span>
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

export default DepositProductInfoSpecSheetPage;
