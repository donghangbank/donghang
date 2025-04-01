import AICanvas from "@renderer/components/banker/AICanvas";
import Simulator from "@renderer/components/common/simulator/Simulator";
import { Outlet } from "react-router-dom";
import { useVADSTT } from "@renderer/hooks/ai/useVADSTT";
import { useHandleSTTResult } from "@renderer/hooks/ai/useHandleSTTResult";
import { useEffect } from "react";

export const SeniorLayout = (): JSX.Element => {
	const { transcript, start, stop } = useVADSTT();
	const { handleSTT } = useHandleSTTResult();

	useEffect(() => {
		start();
		return (): void => stop();
	}, [start, stop]);

	useEffect(() => {
		if (transcript.trim()) {
			handleSTT(transcript);
		}
	}, [transcript, handleSTT]);

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
