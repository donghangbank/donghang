import { requestPrediction } from "@renderer/api/ai/requestPrediction";
import { AIContext } from "@renderer/contexts/AIContext";
import { UserContext } from "@renderer/contexts/UserContext";
import { useCallback, useContext, useEffect, useRef, useState } from "react";

const MINIMUM_RECORDING_DURATION = 1500;

export function useVADSTT(): {
	transcript: string;
	isDetecting: boolean;
	start: () => void;
	stop: () => void;
} {
	const [transcript, setTranscript] = useState("");
	const [isDetecting, setIsDetecting] = useState(false);
	const audioChunks = useRef<Blob[]>([]);
	const mediaRecorderRef = useRef<MediaRecorder | null>(null);
	const vadTimerRef = useRef<NodeJS.Timeout | null>(null);
	const startTimeRef = useRef<number | null>(null);
	const { setIsTalking } = useContext(UserContext);
	const { setConstruction } = useContext(AIContext);

	const startRecording = useCallback(
		(stream: MediaStream): void => {
			const mediaRecorder = new MediaRecorder(stream);
			mediaRecorderRef.current = mediaRecorder;
			audioChunks.current = [];
			startTimeRef.current = Date.now();

			mediaRecorder.ondataavailable = (event): void => {
				if (event.data.size > 0) {
					audioChunks.current.push(event.data);
				}
			};

			mediaRecorder.onstop = async (): Promise<void> => {
				const duration = startTimeRef.current ? Date.now() - startTimeRef.current : 0;
				startTimeRef.current = null;
				const audioBlob = new Blob(audioChunks.current, { type: "audio/webm" });
				audioChunks.current = [];

				if (duration < MINIMUM_RECORDING_DURATION) {
					console.log(
						`음성 길이 ${duration}ms — 무시됨 (최소 ${MINIMUM_RECORDING_DURATION}ms 필요)`
					);
					return;
				}

				try {
					const { response, construction } = await requestPrediction(audioBlob);
					console.log("STT 결과:", response);
					console.log("예측된 행동:", construction);
					setTranscript(response.user_text || "결과 없음");
					setConstruction(construction);
				} catch (err) {
					console.error("음성 처리 실패:", err);
				}
			};

			mediaRecorder.start();
		},
		[setConstruction]
	);

	const stopRecording = useCallback((): void => {
		mediaRecorderRef.current?.stop();
	}, []);

	const startVAD = useCallback(async (): Promise<void> => {
		setIsDetecting(true);

		const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
		const audioContext = new AudioContext();
		const source = audioContext.createMediaStreamSource(stream);
		const analyser = audioContext.createAnalyser();
		source.connect(analyser);

		const data = new Uint8Array(analyser.fftSize);

		const detectVoice = (): void => {
			analyser.getByteTimeDomainData(data);
			const avg = data.reduce((sum, v) => sum + Math.abs(v - 128), 0) / data.length;

			if (avg > 12) {
				if (!mediaRecorderRef.current || mediaRecorderRef.current.state === "inactive") {
					startRecording(stream);
					setIsTalking(true);
				}

				if (vadTimerRef.current) clearTimeout(vadTimerRef.current);
				vadTimerRef.current = setTimeout(() => {
					stopRecording();
					setIsTalking(false);
				}, 3000); // 3초 무음 시 종료
			}

			requestAnimationFrame(detectVoice);
		};

		detectVoice();
	}, [startRecording, stopRecording, setIsTalking]);

	useEffect(() => {
		if (isDetecting) {
			startVAD();
		}
	}, [isDetecting, startVAD]);

	return {
		transcript,
		isDetecting,
		start: () => setIsDetecting(true),
		stop: () => setIsDetecting(false)
	};
}
