import { registerInstallmentProductAPI } from "@renderer/api/products";
import NumberPad from "@renderer/components/common/general/NumberPad";
import InputPanel from "@renderer/components/common/InputPannel";
import { InputContext } from "@renderer/contexts/InputContext";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatAccountNumber, formatDay } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { AxiosError } from "axios";
import { useCallback, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

export const InstallmentProductInfoDayPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { disabled, setDisabled } = useContext(InputContext);
	const {
		memberId,
		accountProductId,
		password,
		withdrawalAccountNumber,
		payoutAccountNumber,
		amount,
		day,
		setDay,
		setProductName,
		setAccountNumber,
		setAccountBalance,
		setInterestRate,
		setAccountExpiryDate,
		setNextInstallmentScheduleDate
	} = useContext(ProductContext);

	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const [timerKey, setTimerKey] = useState(0);
	const [errorMessage, setErrorMessage] = useState("");

	const disableMasking = true;

	const { mutate: registerInstallmentProduct } = useMutation({
		mutationFn: () =>
			registerInstallmentProductAPI({
				memberId,
				accountProductId,
				password,
				withdrawalAccountNumber,
				payoutAccountNumber,
				amount,
				day,
				disableMasking
			}),
		onSuccess: (data) => {
			setProductName(data.productName);
			setAccountNumber(data.accountNumber);
			setAccountBalance(Number(amount));
			setInterestRate(data.interestRate);
			setNextInstallmentScheduleDate(data.nextInstallmentScheduleDate ?? "");
			setAccountExpiryDate(data.accountExpiryDate);
			setDisabled(true);
			setIsPopupVisible(true);
			navigate("/general/installmentproducts/info/specsheet");
		},
		onError: (error: AxiosError) => {
			console.log(error);
		}
	});

	const handleTimerReset = useCallback(() => {
		setTimerKey((prev) => prev + 1);
	}, []);

	const handleConfirm = useCallback((): void => {
		const parsedDay = day.replace(/^0+/, "") || "0";
		const numDay = Number(parsedDay);

		if (isNaN(numDay) || numDay < 1 || numDay > 31) {
			setErrorMessage("납입일자는 1일부터 31일 사이여야 합니다.");
			setIsPopupVisible(true);
			setDisabled(false);
			return;
		}

		setDay(parsedDay);
		setDisabled(true);
		setIsPopupVisible(true);
	}, [day, setDay, setDisabled]);

	const handlePopupConfirm = useCallback((): void => {
		setIsPopupVisible(false);
		setDisabled(false);
		registerInstallmentProduct();
	}, [setDisabled, registerInstallmentProduct]);

	useEffect(() => {
		window.mainAPI.updateSubType("amount");
	}, []);

	useSubMonitorListeners(
		(newVal) => setDay(newVal),
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
				inputValue={day}
				mainLabel={"납입일자"}
				format={formatDay}
				onResetTimer={handleTimerReset}
			/>
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad setInputValue={setDay} type="day" onConfirm={handleConfirm} />
					</div>
				</div>

				<div className="flex flex-col gap-5 ">
					<div className="flex flex-col gap-1 text-2xl bg-white shadow-custom rounded-3xl p-5">
						<span>가능 납입일자</span>
						<span className="text-blue font-bold">1일 ~ 31일</span>
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
										<span className="font-bold text-blue">{formatDay(day)} </span>
										맞으신가요?
									</span>
									<div className="flex gap-2.5 text-2xl font-bold">
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

export default InstallmentProductInfoDayPage;
