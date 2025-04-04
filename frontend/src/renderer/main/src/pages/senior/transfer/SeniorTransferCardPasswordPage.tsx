import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import { useNavigate } from "react-router-dom";
import { useCallback, useContext, useEffect, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { cardCheckAPI } from "@renderer/api/transfer";
import { InputContext } from "@renderer/contexts/InputContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import NumberPanel from "@renderer/components/common/senior/NumberPanel";
import { formatPassword } from "@renderer/utils/formatters";

export default function SeniorTransferCardPasswordPage(): JSX.Element {
	const navigate = useNavigate();
	const [passwordNotMatch, setPasswordNotMatch] = useState(false);
	const { setSendingAccountNumber, password, setPassword, setDisabled } = useContext(InputContext);
	const cardNumber = "9999999999999999";

	useActionPlay({
		dialogue: "비밀번호 4자리를 입력해주세요!",
		shouldActivate: true,
		avatarState: "idle"
	});

	useActionPlay({
		dialogue: "비밀번호가 틀렸습니다! 비밀번호 4자리를 다시 입력해주세요!",
		shouldActivate: passwordNotMatch,
		avatarState: "idle",
		onComplete: () => {
			numberClear();
			setPasswordNotMatch(false);
		}
	});

	const { mutate: cardCheck } = useMutation({
		mutationFn: () => cardCheckAPI({ cardNumber, password }),
		onSuccess: (data) => {
			setSendingAccountNumber(data?.fullAccountNumber ?? "");
			navigate("/senior/transfer/info/account");
		},
		onError: () => {
			setPasswordNotMatch(true);
		}
	});

	const handleConfirm = useCallback((): void => {
		if (password.length !== 4) {
			return;
		}
		cardCheck();
	}, [cardCheck, password]);

	const numberClear = (): void => {
		setPassword((prev) => {
			if (prev.length === 0) return prev;

			const newValue = "";
			window.mainAPI?.notifyMainNumberChange(newValue);
			return newValue;
		});
	};

	useSubMonitorListeners(
		(newVal) => setPassword(newVal),
		handleConfirm,
		() => navigate("/senior/final")
	);

	useEffect(() => {
		setPassword("");
		setDisabled(false);
	}, [setPassword, setDisabled]);

	return (
		<div className="w-full h-full flex justify-center items-center">
			<div className="flex w-full justify-end items-center mr-24">
				<div className="w-96 h-32">
					<NumberPanel inputValue={password} format={formatPassword} hasError={passwordNotMatch} />
				</div>
			</div>
			<TestButton
				prevRoute="/senior/transfer/card/input"
				nextRoute="/senior/transfer/info/account"
			/>
		</div>
	);
}
