import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useNavigate } from "react-router-dom";

export default function SeniorDepositPaymentPage(): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		dialogue: "현금으로 준비하셨나요?",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate("/senior/deposit/cash/input");
		}
	});

	return <></>;
}
