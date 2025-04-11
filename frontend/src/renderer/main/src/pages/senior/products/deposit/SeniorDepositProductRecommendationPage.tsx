import { AIContext } from "@renderer/contexts/AIContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useAudioToggle } from "@renderer/hooks/ai/useAudioToggle";
import { useCallback, useContext, useEffect, useState } from "react";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
//import want_option from "@renderer/assets/audios/want_option.mp3?url";
import want_detail from "@renderer/assets/audios/want_detail.mp3?url";

export default function SeniorDepositProductRecommendationPage(): JSX.Element {
	const { recommended_account, reason, start, stop } = useAudioToggle();
	const [recommended, setRecommended] = useState(false);
	const { setRecommendAccount } = useContext(AIContext);
	const { setAudioStop, construction, setConstruction } = useContext(AIContext);
	const [trigger, setTrigger] = useState(false);
	const navigate = useNavigate();
	const resetAll = useCallback((): void => {
		setRecommended(false);
		setAudioStop(false);
		setConstruction("etc");
	}, [setAudioStop, setConstruction]);

	// 1.
	useActionPlay({
		//audioFile: want_option,
		dialogue: "원하시는 조건 말씀하시고 다 말씀하셨으면 아래 확인 버튼을 눌러주세요!",
		shouldActivate: true,
		avatarState: "focusBottom",
		animationDelay: 3000,
		onComplete: () => {
			window.mainAPI.send("set-sub-mode", "confirm", { label: "확인" });
			start();
			setAudioStop(true);
			stop(false);
			setTrigger(true);
		}
	});

	const handleConfirm = (): void => {
		stop(true);
		setAudioStop(false);
		window.mainAPI.send("set-sub-mode", "default");
	};

	useSubMonitorListeners(
		() => {},
		handleConfirm,
		() => {}
	);

	// const handleConfirm = useCallback((): void => {
	// 	stop(true);
	// 	setAudioStop(false);
	// 	window.mainAPI.send("set-sub-mode", "default");
	// }, [stop, setAudioStop]);

	// 2.
	useActionPlay({
		audioFile: want_detail,
		dialogue: `${recommended_account} 추천드립니다! 더 자세한 정보 원하시나요?`,
		shouldActivate: !!recommended_account && !!reason && trigger,
		avatarState: "idle",
		onComplete: () => {
			setConstruction("etc");
			setRecommended(true);
		}
	});

	useEffect(() => {
		if (!recommended) return;
		if (construction === "긍정") {
			resetAll();
			navigate("/senior/depositproducts/recommendation/detail");
		} else if (construction === "부정") {
			resetAll();
			navigate("/senior/depositproducts/recommendation/question");
		}
	});

	useEffect(() => {
		if (!recommended_account) return;
		setRecommendAccount(recommended_account);
	}, [recommended_account, setRecommendAccount]);

	return (
		<div className="h-full flex items-center">
			{recommended && reason && (
				<motion.div
					initial={{ opacity: 0, y: 20 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 0.5 }}
					className="bg-white p-5 flex flex-col gap-6 mr-24 rounded-3xl absolute right-0 top-[200px] transform -translate-y-1/2"
				>
					<div className="flex flex-col gap-6 justify-center items-start">
						<span className="text-blue text-3xl font-bold">추천 이유</span>
						<div className="bg-cloudyBlue text-3xl leading-10 p-5 text-left rounded-3xl font-bold w-[600px] break-normal whitespace-pre-wrap">
							<span>{reason}</span>
						</div>
					</div>
				</motion.div>
			)}
		</div>
	);
}
