import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { formatAccountNumber } from "@renderer/utils/formatters";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export const TransferInfoAccountPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { account, setAccount, confirmTrigger, setConfirmTrigger, disabled, setDisabled } =
		useContext(InputContext);
	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const [timerKey, setTimerKey] = useState(0);

	const handleTimerReset = useCallback(() => {
		setTimerKey((prev) => prev + 1);
	}, []);

	const handleConfirm = useCallback((): void => {
		if (account.length !== 12) {
			setIsPopupVisible(true);
			setDisabled(false);
			return;
		}
		setDisabled(true);
		setIsPopupVisible(true);
	}, [account, setDisabled]);

	const handlePopupConfirm = useCallback((): void => {
		setIsPopupVisible(false);
		setDisabled(false);
		navigate("/general/transfer/info/amount");
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
				key={`account-${timerKey}`}
				inputValue={account}
				mainLabel={"받으시는 분 계좌번호"}
				format={formatAccountNumber}
				onResetTimer={handleTimerReset}
			/>
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad setInputValue={setAccount} type="account" onConfirm={handleConfirm} />
					</div>
				</div>

				{isPopupVisible && (
					<div className="flex flex-col gap-5 text-3xl bg-white shadow-custom rounded-3xl p-5 h-[40%]">
						{disabled ? (
							<>
								<span>
									<span className="font-bold text-blue">이재백</span>님 맞으신가요?
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
								<span>계좌번호는 12자리여야 합니다</span>
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

export default TransferInfoAccountPage;
