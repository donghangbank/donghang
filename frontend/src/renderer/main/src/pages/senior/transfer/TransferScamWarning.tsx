import ScamWarning from "@renderer/components/common/senior/ScamWarning";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";

export default function TransferScamWarning(): JSX.Element {
	useActionPlay({
		dialogue: "아래 화면을 확인해주세요.",
		shouldActivate: true,
		avatarState: "idle"
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<ScamWarning link="senior/" />
			<TestButton prevRoute="/senior/transfer-check" nextRoute="/senior/transfer-card-warning" />
		</div>
	);
}
