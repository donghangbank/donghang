import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatAccountNumber } from "@renderer/utils/formatters";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export const WithDrawalAccountInputPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { sendingAccountNumber, setSendingAccountNumber, disabled, setDisabled } =
		useContext(InputContext);
	const [timerKey, setTimerKey] = useState(0);

	const handleTimerReset = useCallback(() => {
		setTimerKey((prev) => prev + 1);
	}, []);

	const handlePopupConfirm = useCallback((): void => {
		setDisabled(false);
		navigate("/general/withdrawal/account/password");
	}, [navigate, setDisabled]);

	useEffect(() => {
		window.mainAPI.updateSubType("account");
	}, []);

	const handleConfirm = useCallback((): void => {
		if (sendingAccountNumber.length !== 12) {
			setDisabled(false);
			return;
		}
		handlePopupConfirm();
	}, [sendingAccountNumber, setDisabled, handlePopupConfirm]);

	useSubMonitorListeners(
		(newVal) => setSendingAccountNumber(newVal),
		handleConfirm,
		() => navigate("/general/final")
	);

	useEffect(() => {
		window.mainAPI.updateSubDisabled(disabled);
	}, [disabled]);

	return (
		<div className="flex flex-col gap-6">
			<InputPanel
				key={`account-${timerKey}`}
				inputValue={sendingAccountNumber}
				mainLabel={"계좌번호"}
				format={formatAccountNumber}
				onResetTimer={handleTimerReset}
			/>
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad
							setInputValue={setSendingAccountNumber}
							type="account"
							onConfirm={handleConfirm}
						/>
					</div>
				</div>
			</div>
		</div>
	);
};

export default WithDrawalAccountInputPage;
