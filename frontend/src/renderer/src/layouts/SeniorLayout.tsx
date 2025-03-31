import AICanvas from "@renderer/components/banker/AICanvas";
import Simulator from "@renderer/components/common/simulator/Simulator";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { Outlet } from "react-router-dom";
import intro2 from "@renderer/assets/audios/intro2.mp3?url";
import { AIContext } from "@renderer/contexts/AIContext";
import { useContext, useEffect, useState } from "react";

export const SeniorLayout = (): JSX.Element => {
	const { setAvatarState, setDialogue } = useContext(AIContext);
	const [isReady, setIsReady] = useState(false);

	useEffect(() => {
		const timer = setTimeout(() => {
			setIsReady(true);
		}, 100);

		return (): void => clearTimeout(timer);
	}, []);

	useActionPlay({
		audioFile: intro2,
		dialogue: "필요한게 있으시면 편하게 말씀해주세요.",
		setDialogue,
		setAvatarState,
		shouldActivate: isReady,
		avatarState: "idle"
	});

	return (
		<div className="w-screen h-screen flex flex-col">
			<div className="absolute top-0 left-0 w-full h-[90vh]">
				<AICanvas />
			</div>
			<div className="w-full h-[90%] shadow-md">
				<Outlet />
			</div>

			<div className="w-full h-[10%] bg-gray-400 p-2">
				<Simulator />
			</div>
		</div>
	);
};

export default SeniorLayout;
