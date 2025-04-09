import { requestPrediction } from "@renderer/api/ai/requestPrediction";
import { AIContext } from "@renderer/contexts/AIContext";
import { UserContext } from "@renderer/contexts/UserContext";
import { useCallback, useContext, useEffect, useMemo, useRef, useState } from "react";

const MINIMUM_RECORDING_DURATION = 1500;

export function useVADSTT(externalStream: MediaStream | null = null): {
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
	const rafIdRef = useRef<number | null>(null);
	const streamRef = useRef<MediaStream | null>(null);
	const externalStreamRef = useRef<MediaStream | null>(externalStream);
	const isDetectingRef = useRef<boolean>(false);

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
		if (isDetectingRef.current) return;
		console.log("Starting VAD");
		isDetectingRef.current = true;
		setIsDetecting(true);

		const stream =
			externalStreamRef.current ||
			(await navigator.mediaDevices.getUserMedia({
				audio: {
					noiseSuppression: true,
					echoCancellation: true,
					autoGainControl: true
				}
			}));

		streamRef.current = stream;

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
				}, 1500);
			}
			rafIdRef.current = requestAnimationFrame(detectVoice);
		};

		rafIdRef.current = requestAnimationFrame(detectVoice);
	}, [setIsTalking, startRecording, stopRecording]);

	const stopVAD = useCallback((): void => {
		console.log("Stopping VAD");
		isDetectingRef.current = false;
		setIsDetecting(false);

		if (rafIdRef.current) {
			cancelAnimationFrame(rafIdRef.current);
			rafIdRef.current = null;
		}
		if (vadTimerRef.current) {
			clearTimeout(vadTimerRef.current);
			vadTimerRef.current = null;
		}
		stopRecording();
		if (streamRef.current && !externalStreamRef.current) {
			streamRef.current.getTracks().forEach((track) => track.stop());
			streamRef.current = null;
		}
	}, [stopRecording]);

	const start = useCallback(() => startVAD(), [startVAD]);
	const stop = useCallback(() => stopVAD(), [stopVAD]);

	useEffect(() => {
		return (): void => stopVAD();
	}, [stopVAD]);

	return useMemo(
		() => ({
			transcript,
			isDetecting,
			start,
			stop
		}),
		[transcript, isDetecting, start, stop]
	);
}
