import { useContext, useEffect, useRef, useState } from "react";
import { UserContext } from "../../contexts/UserContext";
import { useWebSocket } from "../websocket/useWebSocket";
import { responseMsg } from "../websocket/socketMsg";

export function useAIAnalysis(
	videoRef: React.RefObject<HTMLVideoElement>,
	canvasRef: React.RefObject<HTMLCanvasElement>
): void {
	const { setIsElderly, setIsUsingPhone } = useContext(UserContext);

	const ageBufferRef = useRef<number[]>([]);
	const [ageConfirmed, setAgeConfirmed] = useState(false);

	const { send } = useWebSocket("ws://localhost:8000/ws/video", {
		onMessage: (data: responseMsg) => {
			const ageIndex = Number(data.predicted_age);

			if (!ageConfirmed && !isNaN(ageIndex) && ageIndex !== 0) {
				const next = [...ageBufferRef.current, ageIndex];
				ageBufferRef.current = next;

				if (next.length >= 3) {
					const frequencyMap = new Map<number, number>();
					next.forEach((val) => {
						frequencyMap.set(val, (frequencyMap.get(val) || 0) + 1);
					});

					let maxFreq = 0;
					let candidates: number[] = [];

					frequencyMap.forEach((freq, val) => {
						if (freq > maxFreq) {
							maxFreq = freq;
							candidates = [val];
						} else if (freq === maxFreq) {
							candidates.push(val);
						}
					});

					candidates.sort((a, b) => a - b);
					const finalAgeIndex = candidates[Math.floor(candidates.length / 2)];

					if (finalAgeIndex >= 8) {
						setIsElderly(true);
					}
					setAgeConfirmed(true);
				}

				return next;
			}

			if (data.calling_detection !== undefined) {
				const isUsingPhone = Boolean(data.calling_detection);
				setIsUsingPhone(isUsingPhone);
			}

			return [];
		}
	});

	// 1초마다 캡처해서 서버로 이미지 전송
	useEffect(() => {
		const interval = setInterval(() => {
			const video = videoRef.current;
			const canvas = canvasRef.current;
			console.log(video, canvas);
			if (!video || !canvas) return;

			const ctx = canvas.getContext("2d");
			ctx?.drawImage(video, 0, 0, canvas.width, canvas.height);

			const imageData = canvas.toDataURL("image/jpeg").split(",")[1];
			send({ image: imageData });
		}, 1000);

		return (): void => clearInterval(interval);
	}, [send, canvasRef, videoRef]);
}
