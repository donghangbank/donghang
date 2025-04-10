import { useContext, useEffect, useMemo, useRef, useCallback } from "react";
import { UserContext } from "../../contexts/UserContext";
import { useWebSocket } from "../websocket/useWebSocket";
import { responseMsg } from "../websocket/socketMsg";

export function useVideoAnalysis(
	videoRef: React.RefObject<HTMLVideoElement>,
	canvasRef: React.RefObject<HTMLCanvasElement>
): void {
	const { isElderly, setIsElderly, setIsUsingPhone, setIsUserExist } = useContext(UserContext);

	const ageBufferRef = useRef<number[]>([]);
	const lastSentTimeRef = useRef<number>(0);
	const continousDetectionRef = useRef<number>(10);

	// Helper function to calculate median age
	const calculateMedianAge = (ages: number[]): number => {
		const sorted = [...ages].sort((a, b) => a - b);
		const middle = Math.floor(sorted.length / 2);
		return sorted.length % 2 !== 0 ? sorted[middle] : (sorted[middle - 1] + sorted[middle]) / 2;
	};

	useEffect(() => {
		if (isElderly === 0) {
			ageBufferRef.current = [];
		}
	}, [isElderly]);

	// 메시지 처리 콜백 분리
	const handleMessage = useCallback(
		(data: responseMsg): void => {
			try {
				// Age detection logic
				if (isElderly === 0 && data.predicted_age !== undefined) {
					const ageIndex = Number(data.predicted_age);
					if (ageIndex !== 0 && !isNaN(ageIndex)) {
						const nextBuffer = [...ageBufferRef.current, ageIndex].slice(-5);
						ageBufferRef.current = nextBuffer;

						if (nextBuffer.length >= 3) {
							const medianAge = calculateMedianAge(nextBuffer);
							if (medianAge >= 6.5) {
								setIsElderly(2);
							} else {
								setIsElderly(1);
							}
						}
					}
				}

				// 유저 존재 여부 판단
				if (data.predicted_age !== undefined) {
					const ageIndex = Number(data.predicted_age);
					if (ageIndex === 0) {
						continousDetectionRef.current += 1;
					} else {
						continousDetectionRef.current = 0;
					}

					setIsUserExist(continousDetectionRef.current < 10);
				}

				// 핸드폰 사용 감지
				if (data.calling_detection !== undefined) {
					setIsUsingPhone(Boolean(data.calling_detection));
				}
			} catch (error) {
				console.error("Error processing video analysis message:", error);
			}
		},
		[isElderly, setIsElderly, setIsUserExist, setIsUsingPhone]
	);

	// WebSocket 옵션 메모이제이션
	const wsOptions = useMemo(
		() => ({
			onMessage: handleMessage,
			onError: (error: Event): void => {
				console.error("Video WebSocket error:", error);
			},
			onClose: (): void => {
				console.log("Video WebSocket connection closed");
			}
		}),
		[handleMessage]
	);

	// WebSocket 연결
	const { send } = useWebSocket("ws://localhost:8000/ws/video", wsOptions);

	// 프레임 전송
	useEffect(() => {
		const captureAndSend = (): void => {
			const now = Date.now();
			if (now - lastSentTimeRef.current < 1000) return;

			const video = videoRef.current;
			const canvas = canvasRef.current;
			if (!video || !canvas || video.readyState < 2) return;

			try {
				const ctx = canvas.getContext("2d");
				if (!ctx) return;

				ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
				const quality = 0.7;
				const imageData = canvas.toDataURL("image/jpeg", quality).split(",")[1];

				if (imageData && send) {
					send({ image: imageData });
					lastSentTimeRef.current = now;
				}
			} catch (error) {
				console.error("Error capturing/sending video frame:", error);
			}
		};

		const interval = setInterval(captureAndSend, 1000);
		return (): void => clearInterval(interval);
	}, [send, videoRef, canvasRef]);

	// Cleanup
	useEffect(() => {
		return (): void => {
			ageBufferRef.current = [];
			setIsElderly(0);
		};
	}, [setIsElderly]);
}
