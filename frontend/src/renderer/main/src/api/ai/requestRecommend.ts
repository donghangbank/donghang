type AccountRecommendation = {
	account: string;
	reason: string;
};

type RecommendationResponse = {
	user_text: string;
	recommendation: AccountRecommendation;
};

export async function requestRecommendation(
	audio: Blob
): Promise<{ response: RecommendationResponse }> {
	try {
		const formData = new FormData();
		formData.append("file", audio, "audio.webm");

		const response = await fetch("https://stirred-solely-hippo.ngrok-free.app/recommend", {
			method: "POST",
			body: formData
		});

		if (!response.ok) {
			const errorData = await response.json();
			throw new Error(errorData.detail || "서버 오류 발생");
		}

		const result: RecommendationResponse = await response.json();

		return {
			response: {
				user_text: result.user_text,
				recommendation: result.recommendation
			}
		};
	} catch (error) {
		console.error("추천 요청 실패:", error);
		throw error;
	}
}
