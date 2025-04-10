import { ProductContext } from "@renderer/contexts/ProductContext";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import { useCallback, useContext, useEffect, useState } from "react";
import { formatAccountNumber, formatAmount, formatDefault } from "@renderer/utils/formatters";
import InputPanel from "@renderer/components/common/InputPannel";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import { registerDepositProductAPI } from "@renderer/api/products";
import { AxiosError } from "axios";
import { InputContext } from "@renderer/contexts/InputContext";
import { motion } from "framer-motion";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";

export default function SeniorDepositProductInfoAmountPage(): JSX.Element {
	const {
		memberId,
		accountProductId,
		password,
		withdrawalAccountNumber,
		payoutAccountNumber,
		amount,
		minAmount,
		maxAmount,
		accountNumber,
		accountBalance,
		accountExpiryDate,
		setAmount,
		setPassword,
		setAccountNumber,
		setAccountBalance,
		setAccountExpiryDate,
		setWithdrawalAccountNumber,
		setPayoutAccountNumber,
		setMemberId
	} = useContext(ProductContext);
	const { disabled, setDisabled } = useContext(InputContext);
	const [isWrongAmount, setIsWrongAmount] = useState(false);
	const {
		userId,
		receivingAccountNumber,
		password: specSheetpassword
	} = useContext(SpecSheetContext);
	const navigate = useNavigate();

	const disableMasking = true;

	const [isLoaded, setIsLoaded] = useState(false);

	const { mutate: registerDepositProduct } = useMutation({
		mutationFn: () =>
			registerDepositProductAPI({
				memberId,
				accountProductId,
				password,
				withdrawalAccountNumber,
				payoutAccountNumber,
				amount,
				disableMasking
			}),
		onSuccess: (data) => {
			setAccountNumber(data.accountNumber);
			setAccountBalance(data.accountBalance);
			setAccountExpiryDate(data.accountExpiryDate);
			setDisabled(true);
		},
		onError: (error: AxiosError) => {
			console.log(error);
		}
	});

	useEffect(() => {
		if (accountNumber && accountBalance && accountExpiryDate && disabled) {
			navigate("/senior/depositproducts/info/specsheet");
		}
	}, [accountNumber, accountBalance, accountExpiryDate, disabled, navigate]);

	useEffect(() => {
		setMemberId(userId);
		setAccountNumber(receivingAccountNumber);
		setWithdrawalAccountNumber(receivingAccountNumber);
		setPayoutAccountNumber(receivingAccountNumber);
		setPassword(specSheetpassword);

		if (
			withdrawalAccountNumber &&
			minAmount !== undefined &&
			maxAmount !== undefined &&
			amount !== undefined
		) {
			setIsLoaded(true);
		}
	}, [
		receivingAccountNumber,
		setAccountNumber,
		setMemberId,
		setPayoutAccountNumber,
		setWithdrawalAccountNumber,
		setPassword,
		userId,
		specSheetpassword,
		withdrawalAccountNumber,
		minAmount,
		maxAmount,
		amount
	]);

	const handleConfirm = useCallback((): void => {
		const parsedAmount = amount.replace(/^0+/, "") || "0";
		if (Number(parsedAmount) < minAmount || Number(parsedAmount) > maxAmount) {
			setAmount("");
			setIsWrongAmount(true);
			setDisabled(false);
			return;
		}
		if (parsedAmount === "0" || isNaN(Number(parsedAmount))) {
			return;
		}
		setAmount(parsedAmount);
		registerDepositProduct();
		navigate("/senior/depositproducts/info/specsheet");
	}, [amount, minAmount, maxAmount, setAmount, registerDepositProduct, navigate, setDisabled]);

	useSubMonitorListeners(
		(newVal) => setAmount(newVal),
		handleConfirm,
		() => navigate("/senior/final")
	);

	useEffect(() => {
		window.mainAPI.updateSubType("amount");
	}, []);

	useEffect(() => {
		window.mainAPI.updateSubDisabled(disabled);
	}, [disabled]);

	useActionPlay({
		dialogue: "예치금의 금액을 입력해주세요!",
		shouldActivate: true,
		avatarState: "idle"
	});

	useActionPlay({
		dialogue: "예치금의 금액이 맞지 않습니다!",
		shouldActivate: isWrongAmount,
		avatarState: "idle",
		onComplete: () => {
			setIsWrongAmount(false);
		}
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<motion.div
				className="fixed top-[500px] right-24 flex flex-col gap-6 w-[650px] h-32 justify-center items-end mr-24"
				initial={{ opacity: 0 }}
				animate={{ opacity: isLoaded ? 1 : 0 }}
				transition={{ duration: 0.5 }}
			>
				<InputPanel
					inputValue={withdrawalAccountNumber}
					mainLabel={"예치금 출금 계좌"}
					format={formatAccountNumber}
					isCount={false}
				/>
				<InputPanel
					inputValue={`${formatAmount(String(minAmount))}`}
					mainLabel={"최소금액"}
					format={formatDefault}
					isCount={false}
				/>
				<InputPanel
					inputValue={`${formatAmount(String(maxAmount))}`}
					mainLabel={"최대금액"}
					format={formatDefault}
					isCount={false}
				/>
				<InputPanel
					inputValue={amount}
					mainLabel={"예치금"}
					format={formatAmount}
					isCount={false}
				/>
			</motion.div>
		</div>
	);
}
