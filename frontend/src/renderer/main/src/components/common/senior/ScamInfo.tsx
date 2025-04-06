import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import { useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";

interface ScamInfoProps {
	prev: string;
	link: string;
}

export default function ScamInfo({ prev, link }: ScamInfoProps): JSX.Element {
	const navigate = useNavigate();

	useActionPlay({
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

	return (
		<div className="w-full h-full flex justify-center items-center">
			<TestButton prevRoute={prev} nextRoute={link} />
		</div>
	);
}
