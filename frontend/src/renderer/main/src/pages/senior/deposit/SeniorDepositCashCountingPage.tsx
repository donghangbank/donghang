import TestButton from "@renderer/components/common/senior/TestButton";
import { InputContext } from "@renderer/contexts/InputContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function SeniorDepositCashCountingPage(): JSX.Element {
	const navigate = useNavigate();
	const { amount, setAmount } = useContext(InputContext);

	useEffect(() => {
		navigate("/senior/deposit/confirm");
	}, [amount, navigate]);

	useActionPlay({
		dialogue: "현금을 세고있습니다...",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			setAmount("10000");
		}
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton prevRoute="/senior/deposit/cash/input" nextRoute="/senior/deposit/confirm" />
		</div>
	);
}
