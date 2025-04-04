import AICanvas from "@renderer/components/banker/AICanvas";
import Header from "@renderer/components/common/Header";
import Simulator from "@renderer/components/common/simulator/Simulator";
import inputLinkMapping from "@renderer/config/inputLinkMapping";
import { useContext, useEffect } from "react";
import { Outlet, useLocation } from "react-router-dom";
import { useVADSTT } from "@renderer/hooks/ai/useVADSTT";
import { useHandleSTTResult } from "@renderer/hooks/ai/useHandleSTTResult";
import { PageContext } from "@renderer/contexts/PageContext";
import { motion } from "framer-motion";
import TestButton from "@renderer/components/common/senior/TestButton";

declare global {
	interface Window {
		mainAPI: {
			updateSubState: (hasInputLink: boolean) => void;
			onSubNumberUpdate: (callback: (val: string) => void) => void;
			notifyMainNumberChange: (value: string) => void;
			onCallConfirm: (callback: () => void) => void;
			onCallCancel: (callback: () => void) => void;
			removeCallConfirm: (callback: () => void) => void;
			removeCallCancel: (callback: () => void) => void;
			updateSubType: (type: string) => void;
			updateSubDisabled: (value: boolean) => void;
		};
	}
}

export const MainLayout = (): JSX.Element => {
	const location = useLocation();
	const isSenior = location.pathname === "/" || location.pathname.includes("/senior");
	const isSeniorTest = location.pathname.includes("/senior");
	const { currentJob } = useContext(PageContext);

	const inputLink = inputLinkMapping[location.pathname] || "";

	useEffect(() => {
		// inputLink가 바뀔 때마다 “서브 윈도우 상태 변경” IPC 전송
		// 예: inputLink가 존재(true)면 서브 윈도우에서 <MainButton /> 보여주도록
		if (window.mainAPI && typeof window.mainAPI.updateSubState === "function") {
			window.mainAPI.updateSubState(!!inputLink);
		}
	}, [inputLink]);

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

	useEffect(() => {
		const path = location.pathname;
		let type = "";
		if (path.includes("password")) type = "password";
		else if (path.includes("account")) type = "account";
		else if (path.includes("amount")) type = "amount";
		else if (path.includes("resident")) type = "resident";

		if (type) window.mainAPI.updateSubType(type);
	}, [location.pathname]);

	return (
		<div className="w-screen h-screen flex flex-col">
			<div className="w-full h-full relative">
				{isSenior ? (
					<motion.div
						initial={{ opacity: 0 }}
						animate={{ opacity: 100 }}
						exit={{ opacity: 0 }}
						className="w-full h-[90%] shadow-md"
					>
						{currentJob && (
							<div className="fixed top-4 left-4 py-4 px-8 rounded-xl w-44 h-12 bg-blue text-white font-semibold flex justify-center items-center text-2xl z-10">
								{currentJob}
							</div>
						)}
						<div className="absolute w-full h-full -z-10">
							<AICanvas />
						</div>
						<Outlet />
					</motion.div>
				) : (
					<div className="w-full h-[90%] flex flex-col">
						<Header />
						<div className="flex-1 flex">
							<div className="w-[50vw]">
								<AICanvas />
							</div>
							<div className="w-[50vw] flex flex-col items-center justify-center p-10">
								<Outlet />
							</div>
						</div>
					</div>
				)}
				<div className="absolute bottom-0 w-full h-[10%] bg-gray-400 p-2">
					<Simulator />
				</div>
			</div>
			{!isSeniorTest && <TestButton prevRoute="/" nextRoute="/senior" />}
		</div>
	);
};

export default MainLayout;
