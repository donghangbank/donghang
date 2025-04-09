import { depositAPI } from "@renderer/api/deposit";
import { InputContext } from "@renderer/contexts/InputContext";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import useKoreaTime from "@renderer/hooks/useKoreaTime";
import { formatAmount } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

export const DepositConfirmPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { amount, receivingAccountNumber } = useContext(InputContext);
	const {
		setAmount: setSpecSheetAmount,
		setReceivingAccountNumber: setSpecSheetReceivingAccountNumber,
		setSendingAccountBalance,
		setTransactionTime
	} = useContext(SpecSheetContext);

	const sessionStartTime = useKoreaTime();
	const disableMasking = true;

	const { mutate: deposit } = useMutation({
		mutationFn: () =>
			depositAPI({
				receivingAccountNumber,
				amount,
				sessionStartTime,
				disableMasking
			}),
		onSuccess: (data) => {
			setSpecSheetAmount(data.amount);
			setSpecSheetReceivingAccountNumber(data.accountNumber);
			setSendingAccountBalance(data.balance);
			setTransactionTime(data.transactionTime);
		},
		onError: (error: AxiosError) => {
			console.log(error);
		}
	});

	const handleConfirm = (): void => {
		deposit();
		navigate("/general/deposit/specsheet");
	};

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col gap-6 bg-white p-10 rounded-3xl shadow-custom"
		>
			<span className="text-5xl font-bold text-center">내용을 확인해주세요</span>
			<div className="flex justify-between gap-20 items-center">
				<span className="text-blue text-3xl font-bold">금액</span>
				<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[510px]">
					<span>{formatAmount(String(amount))}</span>
				</div>
			</div>
			<div className="grid grid-cols-2 gap-5 text-white text-3xl font-bold">
				<button type="button" className="p-8 bg-green rounded-3xl" onClick={handleConfirm}>
					거래 확인
				</button>
				<button type="button" className="p-8 bg-red rounded-3xl">
					<Link to="/general/final">거래 취소</Link>
				</button>
			</div>
		</motion.div>
	);
};

export default DepositConfirmPage;
