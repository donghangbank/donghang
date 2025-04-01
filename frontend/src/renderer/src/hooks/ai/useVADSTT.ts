import { useCallback, useEffect, useRef, useState } from "react";

export function useVADSTT(): {
	transcript;
	isDetecting;
	start: () => void;
	stop: () => void;
} {
	const [transcript, setTranscript] = useState("");
	const [isDetecting, setIsDetecting] = useState(false);
	const audioChunks = useRef<Blob[]>([]);
	const mediaRecorderRef = useRef<MediaRecorder | null>(null);
	const vadTimerRef = useRef<NodeJS.Timeout | null>(null);

	const startRecording = useCallback((stream: MediaStream): void => {
		const mediaRecorder = new MediaRecorder(stream);
		mediaRecorderRef.current = mediaRecorder;

		audioChunks.current = [];

		mediaRecorder.ondataavailable = (event): void => {
			if (event.data.size > 0) {
				audioChunks.current.push(event.data);
			}
		};

		mediaRecorder.onstop = async (): Promise<void> => {
			const audioBlob = new Blob(audioChunks.current, { type: "audio/webm" });
			audioChunks.current = [];

			const formData = new FormData();
			formData.append("file", audioBlob, "audio.webm");
			formData.append("model", "gpt-4o-mini-transcribe");
			formData.append("language", "ko");
			formData.append(
				"prompt",
				"The following conversation is about banking services. Please provide a summary of the conversation in Korean."
			);

			const response = await fetch("https://api.openai.com/v1/audio/transcriptions", {
				method: "POST",
				headers: {
					Authorization: `Bearer ${import.meta.env.VITE_OPENAI_API_KEY as string}`
				},
				body: formData
			});

			const data = await response.json();
			setTranscript(data.text || "결과 없음");
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

			if (avg > 5) {
				// 감지 시작
				if (!mediaRecorderRef.current || mediaRecorderRef.current.state === "inactive") {
					startRecording(stream);
				}

				// 타이머 초기화
				if (vadTimerRef.current) clearTimeout(vadTimerRef.current);

				// 일정 시간 무음이면 종료
				vadTimerRef.current = setTimeout(() => {
					stopRecording();
				}, 2000); // 2초 무음
			}

			requestAnimationFrame(detectVoice);
		};

		detectVoice();
	}, [startRecording, stopRecording]);

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
