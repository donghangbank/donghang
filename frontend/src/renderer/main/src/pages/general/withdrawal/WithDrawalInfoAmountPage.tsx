import { withdrawalAPI } from "@renderer/api/withdrawal";
import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import useKoreaTime from "@renderer/hooks/useKoreaTime";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatAmount } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export const WithDrawalInfoAmountPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { receivingAccountNumber, amount, setAmount, disabled, setDisabled } =
		useContext(InputContext);
	const {
		setAmount: setSpecSheetAmount,
		setReceivingAccountNumber: setSpecSheetReceivingAccountNumber,
		setSendingAccountBalance,
		setTransactionTime
	} = useContext(SpecSheetContext);
	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const [timerKey, setTimerKey] = useState(0);

	const sessionStartTime = useKoreaTime();
	const disableMasking = true;

	const { mutate: withdrawal } = useMutation({
		mutationFn: () =>
			withdrawalAPI({
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
			setDisabled(true);
			setIsPopupVisible(true);
		},
		onError: (error: AxiosError) => {
			console.log(error);
		}
	});

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
		withdrawal();
		navigate("/general/withdrawal/cash/output");
	}, [navigate, setDisabled, withdrawal]);

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
				key={`amount-${timerKey}`}
				inputValue={amount}
				mainLabel={"출금 금액"}
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
									출금하시겠어요?
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

export default WithDrawalInfoAmountPage;
