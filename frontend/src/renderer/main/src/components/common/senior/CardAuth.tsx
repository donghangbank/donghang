import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import card_check from "@renderer/assets/audios/card_check.mp3?url";
import { useNavigate } from "react-router-dom";

interface CardAuthProps {
	link: string;
}

export default function CardAuth({ link }: CardAuthProps): JSX.Element {
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

	return <></>;
}
