import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import method1 from "@renderer/assets/audios/method1.mp3";
import method2 from "@renderer/assets/audios/method2.mp3";
import { useState } from "react";

export default function TransferOptionSeniorPage(): JSX.Element {
	const [playSecond, setPlaySecond] = useState(false);

	useActionPlay({
		audioFile: method1,
		dialogue: "어떤 방법으로 보내시겠어요?",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => setPlaySecond(true)
	});

	useActionPlay({
		audioFile: method2,
		dialogue: "카드, 통장, 계좌이체 가능합니다!",
		shouldActivate: playSecond,
		avatarState: "idle"
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton prevRoute="/senior/transfer-card-warning" nextRoute="/senior" />
		</div>
	);
}
