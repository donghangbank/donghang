import { AIContext } from "@renderer/contexts/AIContext";
import { UserContext } from "@renderer/contexts/UserContext";
import { useContext, useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useNavigate } from "react-router-dom";

export default function VoiceFishingDetector(): JSX.Element {
	const [isFishing, setIsFishing] = useState(false);
	const { construction } = useContext(AIContext);
	const { isNotFishing, setIsNotFishing } = useContext(UserContext);
	const navigate = useNavigate();

	useEffect(() => {
		if (isNotFishing) return;
		if (construction === "보이스피싱") {
			setIsFishing(true);
		}
	}, [construction, isNotFishing]);

	const handleConfirm = (): void => {
		setIsFishing(false);
		setIsNotFishing(false);
		setTimeout(() => navigate("/senior/final"), 300);
	};

	const handleCancel = (): void => {
		setIsFishing(false);
		setIsNotFishing(true);
	};

	return (
		<AnimatePresence>
			{isFishing && !isNotFishing && (
				<motion.div
					initial={{ opacity: 0, y: -20 }}
					animate={{ opacity: 1, y: 0 }}
					exit={{ opacity: 0, y: 20 }}
					transition={{ duration: 0.3 }}
					className="w-screen h-screen flex justify-center items-center bg-transparent"
				>
					<motion.div
						initial={{ boxShadow: "inset 0 0px 0px rgba(255,0,0,0.0)" }}
						animate={{
							boxShadow: [
								"inset 0 0px 0px rgba(255,0,0,0.0)",
								"inset 0 0px 100px rgba(255,0,0,0.4)",
								"inset 0 0px 180px rgba(255,0,0,0.5)"
							]
						}}
						transition={{
							duration: 1.5,
							ease: "easeInOut",
							times: [0, 0.5, 1],
							repeat: Infinity,
							repeatType: "reverse"
						}}
						className="w-full h-full"
					>
						<div className="w-full h-full flex justify-center items-center">
							<div className="bg-white rounded-3xl shadow-custom flex flex-col items-center text-4xl font-bold gap-8 p-6">
								<div className="text-red">보이스 피싱이 의심됩니다!</div>
								<div className="w-full flex gap-4">
									<button className="flex-1" onClick={handleConfirm}>
										거래 취소
									</button>
									<button onClick={handleCancel} className="flex-1">
										거래 계속
									</button>
								</div>
							</div>
						</div>
					</motion.div>
				</motion.div>
			)}
		</AnimatePresence>
	);
}
