import ScamWarning from "@renderer/components/senior/commons/ScamWarning";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import transfer_check from "@renderer/assets/audios/transfer_check.mp3?url";

export default function TransferScamWarning(): JSX.Element {
	useActionPlay({
		audioFile: transfer_check,
		dialogue: "금융사기를 조심하시길 바랍니다",
		setDialogue: () => {},
		setAvatarState: () => {},
		shouldActivate: true,
		avatarState: "idle"
	});

	return (
		<div className="w-full h-full flex justify-center items-center">
			<ScamWarning link="senior/" />
		</div>
	);
}
