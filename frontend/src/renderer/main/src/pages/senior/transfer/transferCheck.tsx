import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import transfer_check from "@renderer/assets/audios/transfer_check.mp3?url";
import { useContext, useEffect } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { useNavigate } from "react-router-dom";
import TestButton from "@renderer/components/common/senior/TestButton";
import { PageContext } from "@renderer/contexts/PageContext";

export default function TransferCheck(): JSX.Element {
	const { construction } = useContext(AIContext);
	const { setCurrentJob } = useContext(PageContext);
	const navigate = useNavigate();

	useActionPlay({
		audioFile: transfer_check,
		dialogue: "이체 도와드리면 될까요?",
		shouldActivate: true,
		avatarState: "idle"
	});

	useEffect(() => {
		if (construction === "긍정") {
			navigate("/senior/transfer-scam-warning");
			setCurrentJob("이체");
		} else if (construction === "부정" || construction === "홈") {
			navigate("/senior");
		}
	}, [construction, navigate, setCurrentJob]);

	// 버튼 클릭 시 확인용
	useEffect(() => {
		setCurrentJob("이체");
	}, [setCurrentJob]);

	return (
		<>
			<TestButton prevRoute="/senior" nextRoute="/senior/transfer-scam-warning" />
		</>
	);
}
