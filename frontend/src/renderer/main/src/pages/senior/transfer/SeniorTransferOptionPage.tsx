import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import method1 from "@renderer/assets/audios/method1.mp3";
import method2 from "@renderer/assets/audios/method2.mp3";
import { useContext, useEffect, useState } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { useNavigate } from "react-router-dom";

export default function SeniorTransferOptionPage(): JSX.Element {
	const [playSecond, setPlaySecond] = useState(false);
	const { construction } = useContext(AIContext);
	const navigate = useNavigate();

	useActionPlay({
		audioFile: method1,
		dialogue: "어떤 방법으로 보내시겠어요?",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => setPlaySecond(true)
	});

	useActionPlay({
		audioFile: method2,
		dialogue: "카드, 통장, 계좌번호 가능합니다!",
		shouldActivate: playSecond,
		avatarState: "idle"
	});

	useEffect(() => {
		if (construction === "카드선택") {
			navigate("/senior/transfer/card/input");
		}
	}, [construction, navigate]);

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton
				prevRoute="/senior/transfer/warning/card"
				nextRoute="/senior/transfer/card/input"
			/>
		</div>
	);
}
