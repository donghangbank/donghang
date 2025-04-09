import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatAccountNumber, formatAmount } from "@renderer/utils/formatters";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

export const InstallmentProductInfoAmountPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { disabled, setDisabled } = useContext(InputContext);
	const { withdrawalAccountNumber, amount, minAmount, maxAmount, setAmount } =
		useContext(ProductContext);

	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const [timerKey, setTimerKey] = useState(0);
	const [errorMessage, setErrorMessage] = useState("");

	const handleTimerReset = useCallback(() => {
		setTimerKey((prev) => prev + 1);
	}, []);

	const handleConfirm = useCallback((): void => {
		const parsedAmount = amount.replace(/^0+/, "") || "0";
		const numAmount = Number(parsedAmount);
		if (isNaN(numAmount) || numAmount < minAmount || numAmount > maxAmount) {
			setErrorMessage("금액 입력 범위가 아닙니다.");
			setIsPopupVisible(true);
			setDisabled(false);
			return;
		}
		setAmount(parsedAmount);
		setDisabled(true);
		setIsPopupVisible(true);
	}, [amount, minAmount, maxAmount, setAmount, setDisabled]);

	const handlePopupConfirm = useCallback((): void => {
		setIsPopupVisible(false);
		setDisabled(false);
		navigate("/general/installmentproducts/info/day");
	}, [setDisabled, setIsPopupVisible, navigate]);

	useEffect(() => {
		window.mainAPI.updateSubType("amount");
	}, []);

	useSubMonitorListeners(
		(newVal) => setAmount(newVal),
		handleConfirm,
		() => navigate("/general/final")
	);

	useEffect(() => {
		window.mainAPI.updateSubDisabled(disabled);
	}, [disabled]);

	return (
		<div className="flex flex-col gap-6">
			<InputPanel
				inputValue={withdrawalAccountNumber}
				mainLabel={"자동이체 계좌"}
				format={formatAccountNumber}
				isCount={false}
			/>
			<InputPanel
				key={`amount-${timerKey}`}
				inputValue={amount}
				mainLabel={"월 납입금"}
				format={formatAmount}
				onResetTimer={handleTimerReset}
			/>
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad setInputValue={setAmount} type="amount" onConfirm={handleConfirm} />
					</div>
				</div>

				<div className="flex flex-col gap-5 ">
					<div className="flex flex-col gap-1 text-2xl bg-white shadow-custom rounded-3xl p-5">
						<span>최소 가능 월 납입액</span>
						<span className="text-blue font-bold">{formatAmount(String(minAmount))}</span>
						<span>최대 가능 월 납입액</span>
						<span className="text-blue font-bold">{formatAmount(String(maxAmount))}</span>
					</div>
					{isPopupVisible && (
						<motion.div
							initial={{ opacity: 0, y: 20 }}
							animate={isPopupVisible ? { opacity: 1, y: 0 } : { opacity: 0 }}
							transition={{ duration: 0.5 }}
							className="flex flex-col text-3xl bg-white shadow-custom rounded-3xl p-5"
						>
							{disabled ? (
								<div className="flex flex-col gap-2.5 font-bold">
									<span>
										<span className="font-bold text-blue">{formatAmount(amount)} </span>
										보내시겠습니까?
									</span>
									<div className="flex gap-2.5 font-bold">
										<button
											type="button"
											className="p-3 bg-green rounded-xl w-full"
											onClick={handlePopupConfirm}
										>
											<span className="text-white">예</span>
										</button>
										<button
											type="button"
											className="p-3 bg-red rounded-xl w-full"
											onClick={() => {
												setIsPopupVisible(false);
												setDisabled(false);
												handleTimerReset();
											}}
										>
											<span className="text-white">아니요</span>
										</button>
									</div>
								</div>
							) : (
								<div className="flex flex-col gap-2.5 font-bold">
									<span>{errorMessage}</span>
									<button
										type="button"
										className="p-5 bg-gray-300 rounded-3xl"
										onClick={() => setIsPopupVisible(false)}
									>
										닫기
									</button>
								</div>
							)}
						</motion.div>
					)}
				</div>
			</div>
		</div>
	);
};

export default InstallmentProductInfoAmountPage;
