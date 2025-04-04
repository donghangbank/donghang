import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import card_check from "@renderer/assets/audios/card_check.mp3?url";
import { useNavigate } from "react-router-dom";

export default function SeniorTransferCheckCardPage(): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		audioFile: card_check,
		dialogue: "잠시 카드 확인해볼게요!",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate("/senior/transfer/card/password");
		}
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton
				prevRoute="/senior/transfer/card/input"
				nextRoute="/senior/transfer/card/password"
			/>
		</div>
	);
}
