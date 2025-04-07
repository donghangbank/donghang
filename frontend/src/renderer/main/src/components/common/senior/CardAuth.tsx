import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import card_check from "@renderer/assets/audios/card_check.mp3?url";
import { useNavigate } from "react-router-dom";

interface CardAuthProps {
	prev: string;
	link: string;
}

export default function CardAuth({ prev, link }: CardAuthProps): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		audioFile: card_check,
		dialogue: "잠시 카드 확인해볼게요!",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate(link);
		}
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton prevRoute={prev} nextRoute={link} />
		</div>
	);
}
