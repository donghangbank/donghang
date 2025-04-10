import { Construction, CONSTRUCTION_VALUES } from "@renderer/contexts/AIContext";

type PredictionResponse = {
	user_text: string;
	predicted_action: string;
};

export async function requestPrediction(
	audio: Blob
): Promise<{ construction: Construction; response: PredictionResponse }> {
	try {
		const formData = new FormData();
		formData.append("file", audio, "audio.webm");

		const response = await fetch(import.meta.env.VITE_PYTHON_API_URL + "prediction", {
			method: "POST",
			body: formData
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.detail || "서버 오류 발생");
		}

		const result: PredictionResponse = await response.json();

		const isValidConstruction = (value: string): value is Construction =>
			CONSTRUCTION_VALUES.includes(value as Construction);

		const construction = isValidConstruction(result.predicted_action)
			? result.predicted_action
			: "etc";

		return { construction, response: result };
	} catch (error) {
		console.error("예측 요청 실패:", error);
		throw error;
	}
}
