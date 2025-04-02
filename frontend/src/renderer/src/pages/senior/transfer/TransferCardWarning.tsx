import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import transfer_check from "@renderer/assets/audios/transfer_check.mp3?url";
import CardDuplicateWarning from "@renderer/components/senior/commons/CardDuplicateWarning";

export default function TransferCardWaring(): JSX.Element {
	useActionPlay({
		audioFile: transfer_check,
		dialogue: "카드 복제에 주의하시길 바랍니다",
		setDialogue: () => {},
		setAvatarState: () => {},
		shouldActivate: true,
		avatarState: "idle"
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<CardDuplicateWarning link="adf" />
		</div>
	);
}
