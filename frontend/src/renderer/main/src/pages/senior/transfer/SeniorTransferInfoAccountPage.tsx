import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
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
	const [constructionTrigger, setConstructionTrigger] = useState(false);
	const { setCurrentJob } = useContext(PageContext);
	const navigate = useNavigate();

	const reset = useCallback((): void => {
		setAccountNotMatch(false);
		setFirstInput(true);
		setOwnerConfirmedTrigger(false);
		setConstructionTrigger(false);
		setDisabled(false);
	}, [
		setAccountNotMatch,
		setFirstInput,
		setOwnerConfirmedTrigger,
		setConstructionTrigger,
		setDisabled
	]);

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

	// 12 자리 계좌번호 입력 시, 계좌주 확인 API 호출
	const handleConfirm = useCallback((): void => {
		if (receivingAccountNumber.length !== 12) {
			setDisabled(false);
			return;
		}
		accountOwnerCheck();
	}, [receivingAccountNumber, setDisabled, accountOwnerCheck]);

	// 서브 모니터 이벤트 리스너 등록
	useSubMonitorListeners(
		(newVal) => setReceivingAccountNumber(newVal),
		handleConfirm,
		() => navigate("/senior/final")
	);

	// 계좌번호 초기화
	const numberClear = useCallback((): void => {
		setReceivingAccountNumber((prev) => {
			if (prev.length === 0) return prev;

			const newValue = "";
			window.mainAPI?.notifyMainNumberChange(newValue);
			return newValue;
		});
	}, [setReceivingAccountNumber]);

	// 계좌번호 입력 시, 계좌주 확인 API 호출
	useEffect(() => {
		if (!isAccountNotMatch && data?.ownerName) {
			setOwnerConfirmedTrigger(false);
			setTimeout(() => {
				setOwnerConfirmedTrigger(true);
				setConstruction("etc");
			}, 200);
		}
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [data?.ownerName]);

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
		avatarState: "idle",
		onComplete: () => {
			setConstruction("etc");
			setConstructionTrigger(true);
		}
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
		if (!constructionTrigger) return;
		if (construction === "긍정") {
			navigate("/senior/transfer/info/amount");
		} else if (construction === "부정") {
			numberClear();
			reset();
		}
	}, [
		construction,
		setConstruction,
		navigate,
		setCurrentJob,
		numberClear,
		constructionTrigger,
		reset
	]);

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
		</div>
	);
}
