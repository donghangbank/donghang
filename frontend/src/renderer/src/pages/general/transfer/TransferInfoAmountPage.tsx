import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { formatAccountNumber, formatAmount } from "@renderer/utils/formatters";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export const TransferInfoAmountPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { account, amount, setAmount, confirmTrigger, setConfirmTrigger, disabled, setDisabled } =
		useContext(InputContext);
	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const [timerKey, setTimerKey] = useState(0);

	const handleTimerReset = useCallback(() => {
		setTimerKey((prev) => prev + 1);
	}, []);

	const handleConfirm = useCallback((): void => {
		const parsedAmount = amount.replace(/^0+/, "") || "0";
		if (parsedAmount === "0" || isNaN(Number(parsedAmount))) {
			setIsPopupVisible(true);
			setDisabled(false);
			return;
		}

		setAmount(parsedAmount);
		setDisabled(true);
		setIsPopupVisible(true);
	}, [amount, setAmount, setDisabled]);

	const handlePopupConfirm = useCallback((): void => {
		setIsPopupVisible(false);
		setDisabled(false);
		navigate("/general/transfer/info/specsheet");
	}, [navigate, setDisabled]);

	useEffect(() => {
		if (confirmTrigger === -1) {
			handleConfirm();
			setConfirmTrigger(0);
		}
	}, [confirmTrigger, setConfirmTrigger, handleConfirm]);

	return (
		<div className="flex flex-col gap-6">
			<InputPanel
				inputValue={account}
				mainLabel={"받으시는 분 계좌번호"}
				format={formatAccountNumber}
				isCount={false}
			/>
			<InputPanel
				key={`amount-${timerKey}`}
				inputValue={amount}
				mainLabel={"금액"}
				format={formatAmount}
				onResetTimer={handleTimerReset}
			/>
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad setInputValue={setAmount} type="amount" onConfirm={handleConfirm} />
					</div>
				</div>

				{isPopupVisible && (
					<div className="flex flex-col gap-5 text-3xl bg-white shadow-custom rounded-3xl p-5 h-[45%]">
						{disabled ? (
							<>
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
							</>
						) : (
							<>
								<span>0원 초과 입력해주세요</span>
								<button
									type="button"
									className="p-5 bg-gray-300 rounded-3xl"
									onClick={() => setIsPopupVisible(false)}
								>
									닫기
								</button>
							</>
						)}
					</div>
				)}
			</div>
		</div>
	);
};

export default TransferInfoAmountPage;
