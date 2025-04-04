import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import CardDuplicateWarning from "@renderer/components/common/senior/CardDuplicateWarning";
import TestButton from "@renderer/components/common/senior/TestButton";

export default function SeniorTransferCardWaringPage(): JSX.Element {
	useActionPlay({
		dialogue: "카드 복제에 주의하시길 바랍니다",
		shouldActivate: true,
		avatarState: "idle"
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<CardDuplicateWarning link="/senior/transfer/option" />
			<TestButton prevRoute="/senior/transfer/warning/scam" nextRoute="/senior/transfer/option" />
		</div>
	);
}
