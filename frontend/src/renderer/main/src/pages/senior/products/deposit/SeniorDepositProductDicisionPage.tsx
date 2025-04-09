import TestButton from "@renderer/components/common/senior/TestButton";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";

export default function SeniorDepositProductDicisionPage(): JSX.Element {
	useActionPlay({
		dialogue: "해당 예금 상품으로 가입 진행해 드릴까요?",
		shouldActivate: true,
		avatarState: "idle"
	});

	return (
		<>
			<TestButton
				prevRoute="/senior/depositproducts/recommendation"
				nextRoute="/senior/depositproducts/option"
			/>
		</>
	);
}
