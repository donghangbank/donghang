import { registerDemandProductAPI } from "@renderer/api/products";
import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatPassword } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export const DemandProductInfoPasswordPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { disabled, setDisabled } = useContext(InputContext);
	const {
		memberId,
		accountProductId,
		password,
		setProductName,
		setPassword,
		setAccountNumber,
		setAccountBalance,
		setInterestRate,
		setAccountExpiryDate
	} = useContext(ProductContext);

	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const [timerKey, setTimerKey] = useState(0);
	const [errorMessage, setErrorMessage] = useState("");

	const disableMasking = true;

	const { mutate: registerDemandProduct } = useMutation({
		mutationFn: () =>
			registerDemandProductAPI({
				memberId,
				accountProductId,
				password,
				disableMasking
			}),
		onSuccess: (data) => {
			setProductName(data.productName);
			setAccountNumber(data.accountNumber);
			setAccountBalance(data.accountBalance);
			setInterestRate(data.interestRate);
			setAccountExpiryDate(data.accountExpiryDate);
			setDisabled(true);
			setIsPopupVisible(true);
			navigate("/general/demandproducts/info/specsheet");
		},
		onError: (error: AxiosError) => {
			console.log(error);
		}
	});

	const handleTimerReset = useCallback(() => {
		setTimerKey((prev) => prev + 1);
	}, []);

	const handleConfirm = useCallback((): void => {
		if (password.length !== 4) {
			setErrorMessage("4자리 비밀번호를 입력해주세요");
			setIsPopupVisible(true);
			setDisabled(false);
			return;
		}
		setDisabled(true);
		setIsPopupVisible(true);
	}, [setDisabled, password]);

	const handlePopupConfirm = useCallback((): void => {
		setIsPopupVisible(false);
		setDisabled(false);
		registerDemandProduct();
	}, [setDisabled, registerDemandProduct]);

	useEffect(() => {
		window.mainAPI.updateSubType("password");
	}, []);

	useSubMonitorListeners(
		(newVal) => setPassword(newVal),
		handleConfirm,
		() => navigate("/general/final")
	);

	useEffect(() => {
		window.mainAPI.updateSubDisabled(disabled);
	}, [disabled]);

	return (
		<div className="flex flex-col gap-6">
			<InputPanel
				key={`password-${timerKey}`}
				inputValue={password}
				mainLabel={"비밀번호"}
				format={formatPassword}
				onResetTimer={handleTimerReset}
			/>
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad setInputValue={setPassword} type="password" onConfirm={handleConfirm} />
					</div>
				</div>

				<div className="flex flex-col gap-5 ">
					{isPopupVisible && (
						<div className="flex flex-col text-3xl bg-white shadow-custom rounded-3xl p-5">
							{disabled ? (
								<div className="flex flex-col gap-2.5 font-bold">
									<span>
										<span className="font-bold text-blue">계좌 개설 </span>
										하시겠어요?
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
						</div>
					)}
				</div>
			</div>
		</div>
	);
};

export default DemandProductInfoPasswordPage;
