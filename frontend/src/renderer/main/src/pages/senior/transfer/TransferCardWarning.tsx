import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import transfer_check from "@renderer/assets/audios/transfer_check.mp3?url";
import CardDuplicateWarning from "@renderer/components/common/senior/CardDuplicateWarning";
import TestButton from "@renderer/components/common/senior/TestButton";

export default function TransferCardWaring(): JSX.Element {
	useActionPlay({
		audioFile: transfer_check,
		dialogue: "카드 복제에 주의하시길 바랍니다",
		shouldActivate: true,
		avatarState: "idle"
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<CardDuplicateWarning link="adf" />
			<TestButton prevRoute="/senior/transfer-scam-warning" nextRoute="/senior/transfer-option" />
		</div>
	);
}
