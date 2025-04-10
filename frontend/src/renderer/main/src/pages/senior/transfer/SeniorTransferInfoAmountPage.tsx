import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import NumberPanel from "@renderer/components/common/senior/NumberPanel";
import how_much from "@renderer/assets/audios/how_much.mp3?url";
import { formatAmount, formatDefault } from "@renderer/utils/formatters";
import { useCallback, useContext, useEffect, useState } from "react";
import { InputContext } from "@renderer/contexts/InputContext";
import { useMutation } from "@tanstack/react-query";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { useNavigate } from "react-router-dom";
import { AxiosError } from "axios";
import { AIContext } from "@renderer/contexts/AIContext";
import { PageContext } from "@renderer/contexts/PageContext";
import { accountOwnerCheckAPI, transferAPI } from "@renderer/api/transfer";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import useKoreaTime from "@renderer/hooks/useKoreaTime";

export default function SeniorTransferInfoAmountPage(): JSX.Element {
	const [firstInput, setFirstInput] = useState(true);
	const { sendingAccountNumber, receivingAccountNumber, amount, setAmount, disabled, setDisabled } =
		useContext(InputContext);
	const [isAmountConfirmed, setIsAmountConfirmed] = useState(false);
	const [trigger, setTrigger] = useState(false);
	const {
		setAmount: setSpecSheetAmount,
		setReceivingAccountNumber: setSpecSheetReceivingAccountNumber,
		setRecipientName,
		setSendingAccountBalance,
		setTransactionTime
	} = useContext(SpecSheetContext);
	const { construction, setConstruction } = useContext(AIContext);
	const { setCurrentJob } = useContext(PageContext);
	const navigate = useNavigate();
	const description = "이체";
	const sessionStartTime = useKoreaTime();
	const disableMasking = true;

	const { mutate: transfer } = useMutation({
		mutationFn: () =>
			transferAPI({
				sendingAccountNumber,
				receivingAccountNumber,
				amount,
				description,
				sessionStartTime,
				disableMasking
			}),
		onSuccess: (data) => {
			setSpecSheetAmount(data.amount);
			setSpecSheetReceivingAccountNumber(data.receivingAccountNumber);
			setRecipientName(data.recipientName);
			setSendingAccountBalance(data.sendingAccountBalance);
			setTransactionTime(data.transactionTime);
			setDisabled(true);
		},
		onError: (error: AxiosError) => {
			console.log(error);
		}
	});

	const { mutate: accountOwnerCheck, data } = useMutation({
		mutationFn: () => accountOwnerCheckAPI({ receivingAccountNumber }),
		onSuccess: () => {
			setDisabled(true);
		},
		onError: (error: AxiosError) => {
			console.error(error);
		}
	});

	const handleConfirm = useCallback((): void => {
		const parsedAmount = amount.replace(/^0+/, "") || "0";
		if (parsedAmount === "0" || isNaN(Number(parsedAmount))) {
			setDisabled(false);
			return;
		}
		accountOwnerCheck();
		setAmount(parsedAmount);
		setConstruction("etc");
	}, [amount, accountOwnerCheck, setAmount, setConstruction, setDisabled]);

	useEffect(() => {
		if (data?.ownerName) {
			setIsAmountConfirmed(true);
		}
	}, [data]);

	useEffect(() => {
		window.mainAPI.updateSubType("amount");
		setDisabled(false);
	}, [setDisabled]);

	useEffect(() => {
		window.mainAPI.updateSubDisabled(disabled);
	}, [disabled]);

	useSubMonitorListeners(
		(newVal) => setAmount(newVal),
		handleConfirm,
		() => navigate("/senior/final")
	);

	const numberClear = useCallback((): void => {
		setAmount((prev) => {
			if (prev.length === 0) return prev;

			const newValue = "";
			window.mainAPI?.notifyMainNumberChange(newValue);
			return newValue;
		});
	}, [setAmount]);

	useActionPlay({
		audioFile: how_much,
		dialogue: "얼마를 보내시겠어요?",
		shouldActivate: firstInput,
		avatarState: "idle",
		onComplete: () => {
			setFirstInput(false);
		}
	});

	useActionPlay({
		dialogue: `${data?.ownerName} 님께 ${amount} 원 보내겠습니다. 맞나요?`,
		shouldActivate: !!data?.ownerName && isAmountConfirmed,
		avatarState: "idle",
		onComplete: () => {
			setConstruction("etc");
			setTrigger(true);
		}
	});

	useEffect(() => {
		if (!trigger) return;
		if (construction === "긍정") {
			transfer();
			navigate("/senior/transfer/info/specsheet");
		} else if (construction === "부정") {
			setIsAmountConfirmed(false);
			numberClear();
			setFirstInput(true);
			setDisabled(false);
		}
	}, [
		construction,
		navigate,
		setCurrentJob,
		numberClear,
		transfer,
		setDisabled,
		isAmountConfirmed,
		trigger
	]);

	useEffect(() => {
		setDisabled(false);
	}, [setDisabled]);

	return (
		<div className="w-full h-full flex justify-center items-center">
			<div className="flex flex-col gap-6 w-full justify-center items-end mr-24">
				{isAmountConfirmed && data?.ownerName && (
					<div className="w-[650px] h-32">
						<NumberPanel inputValue={data?.ownerName} format={formatDefault} />
					</div>
				)}
				<div className="w-[650px] h-32">
					<NumberPanel inputValue={amount} format={formatAmount} />
				</div>
			</div>
		</div>
	);
}
