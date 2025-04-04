import { UserContext } from "@renderer/contexts/UserContext";
import { useContext, useEffect, useRef } from "react";

interface VADOptions {
	onSpeechEnd?: (blob: Blob) => void;
	threshold?: number;
	silenceDuration?: number;
	minSpeechTime?: number;
}

export function useVAD({
	onSpeechEnd,
	threshold = 10,
	silenceDuration = 2000,
	minSpeechTime = 1500
}: VADOptions = {}): { isTalking: boolean } {
	const { isTalking, setIsTalking } = useContext(UserContext);
	const chunksRef = useRef<BlobPart[]>([]);
	const startTimeRef = useRef<number>(0);
	const audioContext = useRef<AudioContext>();
	const silenceTimerRef = useRef<number | null>(null);
	const isTalkingRef = useRef<boolean>(false);

	useEffect(() => {
		let stream: MediaStream;

		// eslint-disable-next-line @typescript-eslint/no-explicit-any
		const init = async (): Promise<any> => {
			stream = await navigator.mediaDevices.getUserMedia({ audio: true });
			audioContext.current = new AudioContext();

			const analyser = audioContext.current.createAnalyser();
			analyser.fftSize = 2048;

			const source = audioContext.current.createMediaStreamSource(stream);
			source.connect(analyser);

			const data = new Uint8Array(analyser.fftSize);

			const recorder = new MediaRecorder(stream);
			recorder.ondataavailable = (e): void => {
				if (e.data.size > 0) {
					chunksRef.current.push(e.data);
				}
			};

			recorder.onstop = (): void => {
				const duration = Date.now() - startTimeRef.current;
				if (duration >= minSpeechTime) {
					const blob = new Blob(chunksRef.current, { type: "audio/webm" });
					onSpeechEnd?.(blob);
				} else {
					console.log("⏱️ 짧은 음성 (무시됨)");
				}
				chunksRef.current = [];
			};

			const intervalId = setInterval(() => {
				analyser.getByteTimeDomainData(data);
				const rms = Math.sqrt(
					data.reduce((acc, val) => acc + Math.pow(val - 128, 2), 0) / data.length
				);

				if (rms > threshold) {
					if (!isTalkingRef.current && recorder.state !== "recording") {
						setIsTalking(true);
						isTalkingRef.current = true;
						startTimeRef.current = Date.now();
						recorder.start();
						console.log("말 시작");
					}

					if (silenceTimerRef.current) {
						clearTimeout(silenceTimerRef.current);
						silenceTimerRef.current = null;
					}
				} else if (isTalkingRef && !silenceTimerRef.current) {
					silenceTimerRef.current = window.setTimeout(() => {
						recorder.stop();
						setIsTalking(false);
						isTalkingRef.current = false;
						console.log("말 끝");
					}, silenceDuration);
				}
			}, 100);

			return () => {
				clearInterval(intervalId);
				stream.getTracks().forEach((track) => track.stop());
				audioContext.current?.close();
			};
		};

		init();

		return (): void => {
			audioContext.current?.close();
		};
	}, []);

	return { isTalking };
}
