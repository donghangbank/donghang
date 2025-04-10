import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import check_screen from "@renderer/assets/audios/check_screen.mp3?url";

interface ScamInfoProps {
	link: string;
}

export default function ScamInfo({ link }: ScamInfoProps): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
		audioFile: check_screen,
		dialogue: "아래 화면을 확인해주세요.",
		shouldActivate: true,
		avatarState: "focusBottom"
	});

	const handleConfirm = useCallback((): void => {
		navigate(link);
	}, [navigate, link]);

	useSubMonitorListeners(
		() => {},
		handleConfirm,
		() => navigate("/senior/final")
	);

	return <></>;
}
