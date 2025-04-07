import TestButton from "@renderer/components/common/senior/TestButton";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";

export default function SeniorDepositProductRecommendationPage(): JSX.Element {
	useActionPlay({
		dialogue: "어떤 예금 상품 가입하길 원하시나요?",
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {}
	});

	return (
		<>
			<TestButton
				prevRoute="/senior/depositproducts/check"
				nextRoute="/senior/depositproducts/dicision"
			/>
		</>
	);
}
