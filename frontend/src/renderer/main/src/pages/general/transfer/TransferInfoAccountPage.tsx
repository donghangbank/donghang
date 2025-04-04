import { accountOwnerCheckAPI } from "@renderer/api/transfer";
import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatAccountNumber } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export const TransferInfoAccountPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { receivingAccountNumber, setReceivingAccountNumber, disabled, setDisabled } =
		useContext(InputContext);
	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const [isErrorPopupVisible, setIsErrorPopupVisible] = useState(false);
	const [timerKey, setTimerKey] = useState(0);
	const [errorMessage, setErrorMessage] = useState("");

	const { mutate: accountOwnerCheck, data } = useMutation({
		mutationFn: () => accountOwnerCheckAPI({ receivingAccountNumber }),
		onSuccess: () => {
			setDisabled(true);
			setIsPopupVisible(true);
		},
		onError: (error: AxiosError) => {
			if (error.response?.status === 404) {
				setErrorMessage("존재하지 않는 계좌번호입니다.");
			} else {
				setErrorMessage("계좌 확인 중 오류가 발생했습니다.");
			}
			setIsErrorPopupVisible(true);
		}
	});

	const handleTimerReset = useCallback(() => {
		setTimerKey((prev) => prev + 1);
	}, []);

	const handleConfirm = useCallback((): void => {
		if (receivingAccountNumber.length !== 12) {
			setIsPopupVisible(true);
			setDisabled(false);
			return;
		}

		accountOwnerCheck();
	}, [receivingAccountNumber, setDisabled, accountOwnerCheck]);

	const handlePopupConfirm = useCallback((): void => {
		setIsPopupVisible(false);
		setDisabled(false);
		navigate("/general/transfer/info/amount");
	}, [navigate, setDisabled]);

	useEffect(() => {
		window.mainAPI.updateSubType("account");
	}, []);

	useSubMonitorListeners(
		(newVal) => setReceivingAccountNumber(newVal),
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
				inputValue={receivingAccountNumber}
				mainLabel={"받으시는 분 계좌번호"}
				format={formatAccountNumber}
				onResetTimer={handleTimerReset}
			/>
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad
							setInputValue={setReceivingAccountNumber}
							type="account"
							onConfirm={handleConfirm}
						/>
					</div>
				</div>

				{isPopupVisible && (
					<div className="flex flex-col gap-5 text-3xl bg-white shadow-custom rounded-3xl p-5 h-[40%]">
						{disabled ? (
							<>
								<span>
									<span className="font-bold text-blue">{data?.ownerName}</span>님 맞으신가요?
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

				{isErrorPopupVisible && (
					<div className="flex flex-col gap-5 text-3xl bg-white shadow-custom rounded-3xl p-5 h-[40%]">
						<span>{errorMessage}</span>
						<button
							type="button"
							className="p-5 bg-gray-300 rounded-3xl"
							onClick={() => setIsErrorPopupVisible(false)}
						>
							닫기
						</button>
					</div>
				)}
			</div>
		</div>
	);
};

export default TransferInfoAccountPage;
