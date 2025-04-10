import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import NumberPanel from "@renderer/components/common/senior/NumberPanel";
import { formatAmount } from "@renderer/utils/formatters";
import { useContext, useEffect, useState } from "react";
import { InputContext } from "@renderer/contexts/InputContext";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { AxiosError } from "axios";
import { AIContext } from "@renderer/contexts/AIContext";
import { PageContext } from "@renderer/contexts/PageContext";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import useKoreaTime from "@renderer/hooks/useKoreaTime";
import { depositAPI } from "@renderer/api/deposit";

export default function SeniorDepositConfirmPage(): JSX.Element {
	const { amount } = useContext(InputContext);
	const {
		setAmount: setSpecSheetAmount,
		receivingAccountNumber,
		setReceivingAccountNumber,
		setTransactionTime,
		recipientName,
		setSendingAccountBalance
	} = useContext(SpecSheetContext);
	const { construction, setConstruction } = useContext(AIContext);
	const { setCurrentJob } = useContext(PageContext);
	const navigate = useNavigate();
	const sessionStartTime = useKoreaTime();
	const disableMasking = true;
	const [canGo, setCanGo] = useState(false);

	const { mutate: deposit } = useMutation({
		mutationFn: () =>
			depositAPI({
				receivingAccountNumber,
				amount,
				sessionStartTime,
				disableMasking
			}),
		onSuccess: (data) => {
			setSpecSheetAmount(Number(data.amount));
			setReceivingAccountNumber(data.accountNumber);
			setSendingAccountBalance(data.balance);
			setTransactionTime(data.transactionTime);
			navigate("/senior/deposit/specsheet");
		},
		onError: (error: AxiosError) => {
			console.log(error);
		}
	});

	useEffect(() => {
		setConstruction("etc");
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	useActionPlay({
		dialogue: `${recipientName} 님 계좌에 ${amount} 원 입금하시는거 맞나요?`,
		shouldActivate: !!recipientName && !!amount,
		avatarState: "idle",
		onComplete: () => {
			setConstruction("etc");
			setCanGo(true);
		}
	});

	useEffect(() => {
		if (!canGo) return;
		if (construction === "긍정") {
			deposit();
		} else if (construction === "부정") {
			navigate("/senior/deposit/card/input");
		}
	}, [construction, navigate, setCurrentJob, deposit, recipientName, amount, canGo]);

	return (
		<div className="w-full h-full flex justify-center items-center">
			<div className="flex flex-col gap-6 w-full justify-center items-end mr-24">
				{recipientName && (
					<div className="w-[650px] h-32">
						<NumberPanel inputValue={amount} format={formatAmount} />
					</div>
				)}
			</div>
		</div>
	);
}
