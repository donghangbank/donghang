import { useCallback, useContext, useEffect, useRef, useState } from "react";
import { requestRecommendation } from "@renderer/api/ai/requestRecommend";
import { UserContext } from "@renderer/contexts/UserContext";

export function useAudioToggle(): {
	recommended_account: string;
	reason: string;
	start: () => void;
	stop: (sendToServer?: boolean) => void;
} {
	const audioChunks = useRef<Blob[]>([]);
	const mediaRecorderRef = useRef<MediaRecorder | null>(null);
	const startTimeRef = useRef<number | null>(null);
	const streamRef = useRef<MediaStream | null>(null);
	const sendToServerRef = useRef<boolean>(false);
	const [recommended_account, setRecommendedAccount] = useState("");
	const [reason, setReason] = useState("");
	const { setIsTalking } = useContext(UserContext);

	const startRecording = useCallback(async () => {
		try {
			const stream = await navigator.mediaDevices.getUserMedia({
				audio: {
					noiseSuppression: true,
					echoCancellation: true,
					autoGainControl: true
				}
			});
			streamRef.current = stream;

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
				startTimeRef.current = null;
				const audioBlob = new Blob(audioChunks.current, { type: "audio/webm" });
				audioChunks.current = [];

				// Only send to server if sendToServerRef is true
				if (sendToServerRef.current) {
					try {
						const { response } = await requestRecommendation(audioBlob);
						console.log("AI 응답:", response);
						setRecommendedAccount(response.recommended_account || "");
						setReason(response.reason || "");
					} catch (err) {
						console.error("음성 처리 실패:", err);
						setRecommendedAccount("");
						setReason("");
					}
				} else {
					console.log("Recording discarded (restart triggered)");
				}
				sendToServerRef.current = false; // Reset the flag
			};

			mediaRecorder.start();
			setIsTalking(true);
		} catch (err) {
			console.error("Failed to start recording:", err);
		}
	}, [setIsTalking]);

	const stopRecording = useCallback(
		(sendToServer: boolean = false): void => {
			setIsTalking(false);
			if (mediaRecorderRef.current && mediaRecorderRef.current.state !== "inactive") {
				sendToServerRef.current = sendToServer; // Set the flag before stopping
				mediaRecorderRef.current.stop();
			}
			if (streamRef.current) {
				streamRef.current.getTracks().forEach((track) => track.stop());
				streamRef.current = null;
			}
		},
		[setIsTalking]
	);

	useEffect(() => {
		return (): void => {
			if (mediaRecorderRef.current && mediaRecorderRef.current.state !== "inactive") {
				mediaRecorderRef.current.stop();
			}
			if (streamRef.current) {
				streamRef.current.getTracks().forEach((track) => track.stop());
			}
		};
	}, []);

	return {
		recommended_account,
		reason,
		start: startRecording,
		stop: stopRecording
	};
}
