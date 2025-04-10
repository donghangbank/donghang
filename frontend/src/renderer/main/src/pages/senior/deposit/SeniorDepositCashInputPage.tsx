import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useNavigate } from "react-router-dom";
import input_cash from "@renderer/assets/audios/input_cash.mp3?url";

export default function SeniorDepositCashInputPage(): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		audioFile: input_cash,
		dialogue: "현금을 입금해주세요!",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate("/senior/deposit/cash/count");
		}
	});

	return <></>;
}
