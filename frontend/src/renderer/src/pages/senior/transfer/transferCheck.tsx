import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import transfer_check from "@renderer/assets/audios/transfer_check.mp3?url";
import { useContext, useEffect } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { useNavigate } from "react-router-dom";

export default function TransferCheck(): JSX.Element {
	const { setAvatarState, setDialogue, construction } = useContext(AIContext);
	const navigate = useNavigate();

	useActionPlay({
		audioFile: transfer_check,
		dialogue: "이체 도와드리면 될까요?",
		setDialogue,
		setAvatarState,
		shouldActivate: true,
		avatarState: "idle"
	});

	useEffect(() => {
		if (construction === "긍정") {
			navigate("/senior/transfer-scam-warning");
		} else if (construction === "부정" || construction === "홈") {
			navigate("/senior");
		}
	}, [construction, navigate]);

	return <></>;
}
