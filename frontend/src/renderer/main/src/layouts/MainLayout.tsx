import AICanvas from "@renderer/components/banker/AICanvas";
import Header from "@renderer/components/common/Header";
import Simulator from "@renderer/components/common/simulator/Simulator";
import { useContext, useEffect, useRef } from "react";
import { Outlet, useLocation } from "react-router-dom";
import { useVADSTT } from "@renderer/hooks/ai/useVADSTT";
import { PageContext } from "@renderer/contexts/PageContext";
import { motion } from "framer-motion";
import { AIContext } from "@renderer/contexts/AIContext";
import { useMediaStream } from "@renderer/hooks/ai/useMediaStream";
import { useVideoAnalysis } from "@renderer/hooks/ai/useVideoAnalysis";
import VoiceFishingDetector from "@renderer/components/common/VoiceFishingDetector";

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
			send: (channel: string, ...args: unknown[]) => void;
		};
	}
}

export const MainLayout = (): JSX.Element => {
	console.log("MainLayout Rendered");
	const location = useLocation();
	const isSenior = location.pathname === "/" || location.pathname.includes("/senior");
	const { currentJob } = useContext(PageContext);
	const { audioStop } = useContext(AIContext);
	const { videoRef, canvasRef, stream } = useMediaStream();
	useVideoAnalysis(videoRef, canvasRef);

	useEffect(() => {
		if (window.mainAPI) {
			const path = location.pathname;
			if (
				path.includes("password") ||
				path.includes("account") ||
				path.includes("amount") ||
				path.includes("day")
			) {
				window.mainAPI.updateSubType(
					path.includes("password")
						? "password"
						: path.includes("account")
							? "account"
							: path.includes("amount")
								? "amount"
								: "day"
				);
				window.mainAPI.send("set-sub-mode", "numpad");
			} else if (path.includes("warning")) {
				if (path.includes("scam")) {
					window.mainAPI.send("set-sub-mode", "scam-warning");
				} else if (path.includes("card")) {
					window.mainAPI.send("set-sub-mode", "card-warning");
				} else {
					window.mainAPI.send("set-sub-mode", "default");
				}
			} else {
				window.mainAPI.send("set-sub-mode", "default");
			}
		}
	}, [location.pathname]);

	const vad = useVADSTT(stream);

	const startRef = useRef(vad.start);
	const stopRef = useRef(vad.stop);

	useEffect(() => {
		if (audioStop && vad.isDetecting) {
			console.log("Stopping VAD");
			stopRef.current();
		} else if (!audioStop && !vad.isDetecting) {
			console.log("Starting VAD");
			startRef.current();
		}
	}, [audioStop, vad.isDetecting, startRef, stopRef]);

	return (
		<div className="w-screen h-screen flex flex-col overflow-hidden">
			<div>
				<video
					ref={videoRef}
					autoPlay
					playsInline
					muted
					width={640}
					height={640}
					className="hidden"
				/>
				<canvas ref={canvasRef} width={640} height={640} className="hidden" />
			</div>
			<div className="absolute z-40">
				<VoiceFishingDetector />
			</div>
			<div className="w-full h-full relative">
				{isSenior ? (
					<motion.div
						initial={{ opacity: 0 }}
						animate={{ opacity: 100 }}
						exit={{ opacity: 0 }}
						className="w-full h-[90%] shadow-md"
					>
						{currentJob && (
							<div className="fixed top-4 left-4 py-4 px-8 rounded-xl w-fit h-12 bg-blue text-white font-semibold flex justify-center items-center text-2xl z-10">
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
		</div>
	);
};

export default MainLayout;
