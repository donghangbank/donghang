import { useCallback, useContext, useEffect, useMemo, useRef, useState } from "react";
import { useVAD } from "./useVAD";
import { useWebSocket } from "../websocket/useWebSocket";
import { responseMsg } from "../websocket/socketMsg";
import { UserContext } from "@renderer/contexts/UserContext";

export function useAudioAnalysis(): {
	serverResponse: string;
	wsConnected: boolean;
} {
	const [serverResponse, setServerResponse] = useState("");
	const [wsConnected, setWsConnected] = useState(false);
	const { setUserMsg } = useContext(UserContext);
	const audioQueueRef = useRef<Blob[]>([]);
	const isProcessingRef = useRef(false);

	const wsOptions = useMemo(
		() => ({
			onOpen: (): void => {
				console.log("WebSocket connected");
				setWsConnected(true);
			},
			onMessage: (data: responseMsg): void => {
				console.log("📥 Server response:", data);
				setServerResponse(
					`Input: ${data.user_text}\n🟡 Recommended action: ${data.predicted_action}`
				);
				setUserMsg(data.user_text ?? "");
			},
			onError: (err: Event): void => {
				console.error("WebSocket error:", err);
				setWsConnected(false);
			},
			onClose: (): void => {
				console.log("WebSocket closed");
				setWsConnected(false);
			}
		}),
		[setUserMsg]
	);

	const { send, readyState } = useWebSocket("ws://localhost:8000/ws/audio", wsOptions);

	const processQueue = useCallback(() => {
		if (isProcessingRef.current || audioQueueRef.current.length === 0) return;

		const blob = audioQueueRef.current.shift();
		console.log("blob", blob);
		console.log("readyState", readyState);
		console.log("WebSocket.OPEN", WebSocket.OPEN);
		if (!blob) return;
		console.log("진짜 됐나?");
		isProcessingRef.current = true;
		const reader = new FileReader();

		reader.onloadend = (): void => {
			if (reader.result instanceof ArrayBuffer) {
				try {
					send(reader.result);
					console.log("Audio chunk sent");
				} catch (err) {
					console.error("Failed to send audio:", err);
					// Requeue failed chunk
					if (blob) {
						audioQueueRef.current.unshift(blob);
					}
				}
			}
			isProcessingRef.current = false;
			processQueue(); // Process next item
		};

		reader.onerror = (): void => {
			console.error("FileReader error");
			isProcessingRef.current = false;
			processQueue();
		};

		reader.readAsArrayBuffer(blob);
	}, [send, readyState]);

	const handleSpeechEnd = useCallback(
		(blob: Blob) => {
			console.log("🎙️ Detected speech:", blob.size, "bytes");
			audioQueueRef.current.push(blob);
			processQueue();
		},
		[processQueue]
	);

	useVAD({
		onSpeechEnd: handleSpeechEnd,
		silenceDuration: 1500,
		minSpeechTime: 1000
	});

	// Cleanup on unmount
	useEffect(() => {
		return (): void => {
			audioQueueRef.current = [];
		};
	}, []);

	return { serverResponse, wsConnected };
}
