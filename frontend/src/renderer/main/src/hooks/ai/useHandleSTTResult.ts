import { useContext } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { requestPrediction } from "@renderer/api/ai/requestPrediction";

export function useHandleSTTResult(): { handleSTT: (text: string) => Promise<void> } {
	const { setConstruction } = useContext(AIContext);

	const handleSTT = async (text: string): Promise<void> => {
		try {
			const { construction, response } = await requestPrediction(text);

			setConstruction(construction);

			console.log("예측 결과:", response);
		} catch (e) {
			console.error("예측 처리 중 오류:", e);
		}
	};

	return { handleSTT };
}
