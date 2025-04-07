import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import NumberPanel from "@renderer/components/common/senior/NumberPanel";
import receiver_input from "@renderer/assets/audios/receiver_input.mp3?url";
import { formatAccountNumber } from "@renderer/utils/formatters";
import { useCallback, useContext, useEffect, useState } from "react";
import { InputContext } from "@renderer/contexts/InputContext";
import { useMutation } from "@tanstack/react-query";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { useNavigate } from "react-router-dom";
import { accountOwnerCheckAPI } from "@renderer/api/transfer";
import { AxiosError } from "axios";
import { AIContext } from "@renderer/contexts/AIContext";
import { PageContext } from "@renderer/contexts/PageContext";

export default function SeniorTransferInfoAccountPage(): JSX.Element {
	const [isAccountNotMatch, setAccountNotMatch] = useState(false);
	const [firstInput, setFirstInput] = useState(true);
	const [ownerConfirmedTrigger, setOwnerConfirmedTrigger] = useState(false);
	const { receivingAccountNumber, setReceivingAccountNumber, setDisabled } =
		useContext(InputContext);
	const { construction, setConstruction } = useContext(AIContext);
	const { setCurrentJob } = useContext(PageContext);
	const navigate = useNavigate();

	const { mutate: accountOwnerCheck, data } = useMutation({
		mutationFn: () => accountOwnerCheckAPI({ receivingAccountNumber }),
		onSuccess: () => {
			setDisabled(true);
		},
		onError: (error: AxiosError) => {
			console.error(error);
			setAccountNotMatch(true);
		}
	});

	const handleConfirm = useCallback((): void => {
		if (receivingAccountNumber.length !== 12) {
			setDisabled(false);
			return;
		}
		accountOwnerCheck();
	}, [receivingAccountNumber, setDisabled, accountOwnerCheck]);

	useSubMonitorListeners(
		(newVal) => setReceivingAccountNumber(newVal),
		handleConfirm,
		() => navigate("/senior/final")
	);

	const numberClear = useCallback((): void => {
		setReceivingAccountNumber((prev) => {
			if (prev.length === 0) return prev;

			const newValue = "";
			window.mainAPI?.notifyMainNumberChange(newValue);
			return newValue;
		});
	}, [setReceivingAccountNumber]);

	useEffect(() => {
		if (!isAccountNotMatch && data?.ownerName) {
			setOwnerConfirmedTrigger(false);
			setTimeout(() => setOwnerConfirmedTrigger(true), 100);
		}
	}, [data?.ownerName, isAccountNotMatch]);

	useActionPlay({
		audioFile: receiver_input,
		dialogue: "받으시는 분 계좌번호를 입력해주세요!",
		shouldActivate: firstInput,
		avatarState: "idle",
		onComplete: () => {
			setFirstInput(false);
		}
	});

	useActionPlay({
		dialogue: `${data?.ownerName} 님 맞으신가요?`,
		shouldActivate: ownerConfirmedTrigger,
		avatarState: "idle"
		// onComplete: () => {
		// 	console.log("계좌번호 확인 완료");
		// 	navigate("/senior/transfer/info/amount");
		// }
	});

	useActionPlay({
		dialogue: "계좌번호가 틀렸습니다! 계좌번호를 다시 입력해주세요!",
		shouldActivate: isAccountNotMatch,
		avatarState: "idle",
		onComplete: () => {
			setDisabled(false);
			setAccountNotMatch(false);
			numberClear();
		}
	});

	useEffect(() => {
		if (construction === "긍정") {
			navigate("/senior/transfer/info/amount");
		} else if (construction === "부정") {
			numberClear();
			setFirstInput(true);
		}
	}, [construction, setConstruction, navigate, setCurrentJob, numberClear]);

	useEffect(() => {
		setReceivingAccountNumber("");
	}, [setReceivingAccountNumber]);

	useEffect(() => {
		setDisabled(false);
	}, [setDisabled]);

	return (
		<div className="w-full h-full flex justify-center items-center">
			<div className="flex w-full justify-end items-center mr-24">
				<div className="w-[650px] h-32">
					<NumberPanel
						inputValue={receivingAccountNumber}
						format={formatAccountNumber}
						hasError={isAccountNotMatch}
					/>
				</div>
			</div>
			<TestButton
				prevRoute="/senior/transfer/card/password"
				nextRoute="/senior/transfer/info/amount"
			/>
		</div>
	);
}
