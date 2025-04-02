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
	const startTimeRef = useRef<number | null>(null); // 녹음 시작 시점 저장
	const { setIsTalking } = useContext(UserContext);

	const startRecording = useCallback((stream: MediaStream): void => {
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

			// 짧은 음성 무시
			if (duration < MINIMUM_RECORDING_DURATION) {
				console.log(`음성 길이 ${duration}ms — 무시됨 (최소 ${MINIMUM_RECORDING_DURATION}ms 필요)`);
				return;
			}

			const formData = new FormData();
			formData.append("file", audioBlob, "audio.webm");
			formData.append("model", "gpt-4o-transcribe");
			formData.append("language", "ko");

			try {
				const response = await fetch("https://api.openai.com/v1/audio/transcriptions", {
					method: "POST",
					headers: {
						Authorization: `Bearer ${import.meta.env.VITE_OPENAI_API_KEY as string}`
					},
					body: formData
				});

				const data = await response.json();
				setTranscript(data.text || "결과 없음");
			} catch (err) {
				console.error("음성 인식 요청 실패:", err);
			}
		};

		mediaRecorder.start();
	}, []);

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

			if (avg > 8) {
				if (!mediaRecorderRef.current || mediaRecorderRef.current.state === "inactive") {
					startRecording(stream);
					setIsTalking(true);
				}

				if (vadTimerRef.current) clearTimeout(vadTimerRef.current);

				vadTimerRef.current = setTimeout(() => {
					stopRecording();
					setIsTalking(false);
				}, 2500); // 2.5초 무음 시 종료
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
