import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import { useContext, useEffect } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { useNavigate } from "react-router-dom";

export default function SeniorDepositProductOption(): JSX.Element {
	const { construction } = useContext(AIContext);
	const navigate = useNavigate();

	useActionPlay({
		dialogue: "본인 확인이 필요합니다! 카드, 통장, 계좌번호 중 가지고 오신게 있으실까요?",
		shouldActivate: true,
		avatarState: "idle"
	});

	useEffect(() => {
		if (construction === "카드선택") {
			navigate("/senior/deposit/card/input");
		}
	}, [construction, navigate]);

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton
				prevRoute="/senior/depositproducts/dicision"
				nextRoute="/senior/depositproducts/card/input"
			/>
		</div>
	);
}
