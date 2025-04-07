import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import intro2 from "@renderer/assets/audios/intro2.mp3?url";
import { useNavigate } from "react-router-dom";
import { useContext, useEffect, useState } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import TestButton from "@renderer/components/common/senior/TestButton";

export default function SeniorMain(): JSX.Element {
	const { construction } = useContext(AIContext);
	const [isReady, setIsReady] = useState(false);
	const navigate = useNavigate();

	useEffect(() => {
		const timer = setTimeout(() => {
			setIsReady(true);
		}, 100);

		return (): void => clearTimeout(timer);
	}, []);

	useActionPlay({
		audioFile: intro2,
		dialogue: "필요한게 있으시면 편하게 말씀해주세요.",
		shouldActivate: isReady,
		avatarState: "idle"
	});

	useEffect(() => {
		switch (construction) {
			case "이체":
				navigate("/senior/transfer/check");
				break;
		}
	}, [construction, navigate]);
	return (
		<>
			<TestButton prevRoute="/" nextRoute="/senior/deposit/warning/scam" />
		</>
	);
}
