import { Construction, CONSTRUCTION_VALUES } from "@renderer/contexts/AIContext";

type PredictionResponse = {
	user_text: string;
	predicted_action: string;
};

export async function requestPrediction(
	text: string
): Promise<{ construction: Construction; response: PredictionResponse }> {
	try {
		console.log("예측 요청:", text);
		const response = await fetch(
			`http://localhost:8000/prediction?text=${encodeURIComponent(text)}`,
			{
				method: "POST",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify({ text })
			}
		);

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
