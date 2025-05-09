import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import card_input from "@renderer/assets/audios/card_input.mp3?url";
import { useNavigate } from "react-router-dom";

interface CardAuthProps {
	link: string;
}

export default function CardAuth({ link }: CardAuthProps): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		audioFile: card_input,
		dialogue: "아래 카드 투입구에 카드를 넣어주세요!",
		shouldActivate: true,
		avatarState: "focusBottom",
		onComplete: () => {
			navigate(link);
		}
	});

	return <></>;
}
