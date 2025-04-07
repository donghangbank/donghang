import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import { useNavigate } from "react-router-dom";

export default function SeniorDepositCashInputPage(): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		dialogue: "현금을 입금해주세요!",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate("/senior/deposit/cash/count");
		}
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton prevRoute="/senior/deposit/card/input" nextRoute="/senior/deposit/cash/count" />
		</div>
	);
}
