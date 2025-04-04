/* eslint-disable */

import { UserContext } from "@renderer/contexts/UserContext";
import { useCallback, useContext, useEffect, useRef, useState } from "react";
import { requestPrediction } from "@renderer/api/ai/requestPrediction";

// Use window.require for Node.js modules in Electron renderer
// const fs = window.require("fs").promises;
// const path = window.require("path");

const MINIMUM_RECORDING_DURATION = 1500;
// const AUDIO_DIR = "C:\\donghang\\audio";
const AUDIO_DIR = "audio_recordings"; // 상대 경로 사용

export function useVADSTT(): {
	transcript: string;
	isDetecting: boolean;
	start: () => void;
	stop: () => void;
} {
	const [transcript, setTranscript] = useState("");
	const [isDetecting, setIsDetecting] = useState(false);
	// const audioChunks = useRef<Blob[]>([]);
	// const mediaRecorderRef = useRef<MediaRecorder | null>(null);
	// const vadTimerRef = useRef<NodeJS.Timeout | null>(null);
	// const startTimeRef = useRef<number | null>(null);
	// const { setIsTalking } = useContext(UserContext);
	// const audioFilePathRef = useRef<string | null>(null); // Store file path

	// const startRecording = useCallback(
	// 	(stream: MediaStream): void => {
	// 		const mediaRecorder = new MediaRecorder(stream);
	// 		mediaRecorderRef.current = mediaRecorder;
	// 		audioChunks.current = [];
	// 		startTimeRef.current = Date.now();

	// 		mediaRecorder.ondataavailable = (event): void => {
	// 			if (event.data.size > 0) {
	// 				audioChunks.current.push(event.data);
	// 			}
	// 		};

	// 		mediaRecorder.onstop = async (): Promise<void> => {
	// 			const duration = startTimeRef.current ? Date.now() - startTimeRef.current : 0;
	// 			startTimeRef.current = null;

	// 			const audioBlob = new Blob(audioChunks.current, { type: "audio/webm" });
	// 			audioChunks.current = [];

	// 			if (duration < MINIMUM_RECORDING_DURATION) {
	// 				console.log(
	// 					`음성 길이 ${duration}ms — 무시됨 (최소 ${MINIMUM_RECORDING_DURATION}ms 필요)`
	// 				);
	// 				return;
	// 			}

	// 			try {

	// 				// Send to Python server
	// 				setTranscript(response.user_text || "결과 없음");
	// 			} catch (err) {
	// 				console.error("오디오 처리 실패:", err);
	// 			}
	// 		};

	// 		mediaRecorder.start();
	// 	},
	// 	[]
	// );

	// const stopRecording = useCallback((): void => {
	// 	mediaRecorderRef.current?.stop();
	// }, []);

	// const startVAD = useCallback(async (): Promise<void> => {
	// 	setIsDetecting(true);

	// 	const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
	// 	const audioContext = new AudioContext();
	// 	const source = audioContext.createMediaStreamSource(stream);
	// 	const analyser = audioContext.createAnalyser();
	// 	source.connect(analyser);

	// 	const data = new Uint8Array(analyser.fftSize);

	// 	const detectVoice = (): void => {
	// 		analyser.getByteTimeDomainData(data);
	// 		const avg = data.reduce((sum, v) => sum + Math.abs(v - 128), 0) / data.length;

	// 		if (avg > 12) {
	// 			if (!mediaRecorderRef.current || mediaRecorderRef.current.state === "inactive") {
	// 				startRecording(stream);
	// 				setIsTalking(true);
	// 			}

	// 			if (vadTimerRef.current) clearTimeout(vadTimerRef.current);

	// 			vadTimerRef.current = setTimeout(() => {
	// 				stopRecording();
	// 				setIsTalking(false);
	// 			}, 2500);
	// 		}

	// 		requestAnimationFrame(detectVoice);
	// 	};

	// 	detectVoice();
	// }, [startRecording, stopRecording, setIsTalking]);

	// useEffect(() => {
	// 	if (isDetecting) {
	// 		startVAD();
	// 	}
	// }, [isDetecting, startVAD]);

	return {
		transcript,
		isDetecting,
		start: () => setIsDetecting(true),
		stop: () => setIsDetecting(false)
	};
}
