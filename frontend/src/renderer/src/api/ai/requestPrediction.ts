import { Construction } from "@renderer/contexts/AIContext";

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
		console.log("python 예측 결과:", result);

		const validActions: Construction[] = ["입금", "출금", "이체"];
		const construction = validActions.includes(result.predicted_action as Construction)
			? (result.predicted_action as Construction)
			: "etc";

		return { construction, response: result };
	} catch (error) {
		console.error("예측 요청 실패:", error);
		throw error;
	}
}
