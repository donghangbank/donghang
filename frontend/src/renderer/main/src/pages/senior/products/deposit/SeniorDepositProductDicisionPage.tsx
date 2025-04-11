import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import want_to_make_deposit from "@renderer/assets/audios/want_to_make_deposit.mp3?url";

export default function SeniorDepositProductDicisionPage(): JSX.Element {
	useActionPlay({
		audioFile: want_to_make_deposit,
		dialogue: "해당 예금 상품으로 가입 진행해 드릴까요?",
		shouldActivate: true,
		avatarState: "idle"
	});

	return <></>;
}
