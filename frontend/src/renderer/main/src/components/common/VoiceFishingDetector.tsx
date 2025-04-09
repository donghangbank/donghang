import { AIContext } from "@renderer/contexts/AIContext";
import { UserContext } from "@renderer/contexts/UserContext";
import { useContext, useEffect, useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { useNavigate } from "react-router-dom";

export default function VoiceFishingDetector(): JSX.Element {
	const [isFishing, setIsFishing] = useState(false);
	const [isVisible, setIsVisible] = useState(true);
	const { construction } = useContext(AIContext);
	const { isNotFishing, setIsNotFishing } = useContext(UserContext);
	const navigate = useNavigate();

	useEffect(() => {
		if (isNotFishing) return;
		if (construction === "보이스피싱") {
			setIsFishing(true);
			setIsVisible(true);
		}
	}, [construction, isNotFishing]);

	const handleConfirm = (): void => {
		setIsVisible(false); // 애니메이션 시작
		setTimeout(() => navigate("/senior/final"), 300); // 애니메이션 끝난 후 이동
	};

	return (
		<AnimatePresence>
			{isFishing && isVisible && (
				<motion.div
					initial={{ opacity: 0, y: -20 }}
					animate={{ opacity: 1, y: 0 }}
					exit={{ opacity: 0, y: 20 }}
					className="w-screen h-screen flex justify-center items-center"
				>
					<div className="bg-white rounded-3xl shadow-custom flex flex-col items-center text-4xl font-bold gap-8 p-6">
						<div className="text-red">보이스 피싱이 의심됩니다!</div>
						<div className="w-full flex gap-4">
							<button className="flex-1" onClick={handleConfirm}>
								예
							</button>
							<button
								onClick={() => {
									setIsFishing(false);
									setIsNotFishing(true);
								}}
								className="flex-1"
							>
								아니요
							</button>
						</div>
					</div>
				</motion.div>
			)}
		</AnimatePresence>
	);
}
