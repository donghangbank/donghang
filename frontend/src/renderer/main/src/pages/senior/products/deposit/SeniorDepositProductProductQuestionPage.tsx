import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useNavigate } from "react-router-dom";

export default function SeniorDepositProductProductQuestionPage(): JSX.Element {
	const navigate = useNavigate();
	useActionPlay({
		dialogue: "어떤 예금 상품에 가입하길 원하시나요?",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate("/senior/depositproducts/recommendation/feature");
		}
	});

	return <></>;
}
