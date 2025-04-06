import TestButton from "@renderer/components/common/senior/TestButton";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useNavigate } from "react-router-dom";

export default function SeniorDepositPaymentPage(): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		dialogue: "현금투입구에 지폐를 넣어주세요!",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			navigate("/senior/deposit/cash/input");
		}
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton
				prevRoute="/senior/deposit/card/password"
				nextRoute="/senior/deposit/cash/input"
			/>
		</div>
	);
}
