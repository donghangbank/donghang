import { useContext, useEffect, useMemo, useRef, useState } from "react";
import { UserContext } from "../../contexts/UserContext";
import { useWebSocket } from "../websocket/useWebSocket";
import { responseMsg } from "../websocket/socketMsg";

export function useVideoAnalysis(
	videoRef: React.RefObject<HTMLVideoElement>,
	canvasRef: React.RefObject<HTMLCanvasElement>
): void {
	const { setIsElderly, setIsUsingPhone } = useContext(UserContext);

	// Use a more robust state management for age detection
	const ageBufferRef = useRef<number[]>([]);
	const [ageConfirmed, setAgeConfirmed] = useState(false);
	const lastSentTimeRef = useRef<number>(0);

	// Memoize WebSocket options to prevent unnecessary recreations
	const wsOptions = useMemo(
		() => ({
			onMessage: (data: responseMsg): void => {
				try {
					// Age detection logic
					if (!ageConfirmed && data.predicted_age !== undefined) {
						const ageIndex = Number(data.predicted_age);

						if (!isNaN(ageIndex)) {
							const nextBuffer = [...ageBufferRef.current, ageIndex].slice(-5); // Keep last 5 readings
							ageBufferRef.current = nextBuffer;

							if (nextBuffer.length >= 3) {
								const medianAge = calculateMedianAge(nextBuffer);
								if (medianAge >= 8) {
									setIsElderly(true);
									setAgeConfirmed(true);
								}
							}
						}
					}

					// Phone detection
					if (data.calling_detection !== undefined) {
						setIsUsingPhone(Boolean(data.calling_detection));
					}
				} catch (error) {
					console.error("Error processing video analysis message:", error);
				}
			},
			onError: (error: Event): void => {
				console.error("Video WebSocket error:", error);
			},
			onClose: (): void => {
				console.log("Video WebSocket connection closed");
			}
		}),
		[setIsElderly, setIsUsingPhone, ageConfirmed]
	);

	const { send } = useWebSocket("ws://localhost:8000/ws/video", wsOptions);

	// Helper function to calculate median age
	const calculateMedianAge = (ages: number[]): number => {
		const sorted = [...ages].sort((a, b) => a - b);
		const middle = Math.floor(sorted.length / 2);
		return sorted.length % 2 !== 0 ? sorted[middle] : (sorted[middle - 1] + sorted[middle]) / 2;
	};

	// Throttled image capture and send
	useEffect(() => {
		const captureAndSend = (): void => {
			const now = Date.now();
			if (now - lastSentTimeRef.current < 1000) return; // Throttle to 1 second

			const video = videoRef.current;
			const canvas = canvasRef.current;
			if (!video || !canvas || video.readyState < 2) return; // Check if video is ready

			try {
				const ctx = canvas.getContext("2d");
				if (!ctx) return;

				// Capture frame
				ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

				// Optimize image quality vs size
				const quality = 0.7; // Adjust based on performance
				const imageData = canvas.toDataURL("image/jpeg", quality).split(",")[1];

				if (imageData && send) {
					send({ image: imageData });
					lastSentTimeRef.current = now;
				}
			} catch (error) {
				console.error("Error capturing/sending video frame:", error);
			}
		};

		const interval = setInterval(captureAndSend, 300); // Check more frequently but throttle sends
		return (): void => clearInterval(interval);
	}, [send, videoRef, canvasRef]);

	// Cleanup on unmount
	useEffect(() => {
		return (): void => {
			ageBufferRef.current = [];
			setAgeConfirmed(false);
		};
	}, []);
}
