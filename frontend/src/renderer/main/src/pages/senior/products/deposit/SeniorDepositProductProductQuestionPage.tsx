import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useNavigate } from "react-router-dom";
import what_deposit from "@renderer/assets/audios/what_deposit.mp3?url";

export default function SeniorDepositProductProductQuestionPage(): JSX.Element {
	const navigate = useNavigate();
	useActionPlay({
		audioFile: what_deposit,
		dialogue: "어떤 예금 상품에 가입하길 원하시나요?",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate("/senior/depositproducts/recommendation/feature");
		}
	});

	return <></>;
}
