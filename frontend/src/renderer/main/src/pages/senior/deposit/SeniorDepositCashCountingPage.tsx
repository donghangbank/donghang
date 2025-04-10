import { InputContext } from "@renderer/contexts/InputContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import count_cash from "@renderer/assets/audios/count_cash.mp3?url";

export default function SeniorDepositCashCountingPage(): JSX.Element {
	const navigate = useNavigate();
	const { amount, setAmount } = useContext(InputContext);

	useEffect(() => {
		if (!amount) return;
		console.log("amount", amount);
		navigate("/senior/deposit/confirm");
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [amount]);

	useActionPlay({
		audioFile: count_cash,
		dialogue: "현금을 세고있습니다...",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			setAmount("50000");
		}
	});

	return <></>;
}
