import { useEffect, useRef } from "react";
import { requestMsg, responseMsg } from "./socketMsg";

interface UseWebSocketOptions {
	onOpen?: () => void;
	onMessage?: (data: responseMsg) => void;
	onError?: (err: Event) => void;
	onClose?: () => void;
}

export function useWebSocket(
	url: string,
	options: UseWebSocketOptions
): { send: (payload: requestMsg | ArrayBuffer) => void; readyState: number } {
	const wsRef = useRef<WebSocket | null>(null);

	useEffect(() => {
		const ws = new WebSocket(url);
		wsRef.current = ws;

		ws.onopen = (): void => options.onOpen?.();
		ws.onmessage = (e): void => {
			try {
				const data = JSON.parse(e.data);
				console.log(data);
				options.onMessage?.(data);
			} catch (err) {
				console.error("WebSocket JSON parse error", err);
			}
		};
		ws.onerror = (e): void => {
			console.error("WebSocket error", e);
			options.onError?.(e);
		};
		ws.onclose = (e): void => {
			console.log(`WebSocket closed: ${e.code} ${e.reason}`);
			options.onClose?.();
		};

		return (): void => ws.close();
	}, [url, options]);

	const send = (payload: requestMsg | ArrayBuffer): void => {
		if (wsRef.current?.readyState === WebSocket.OPEN) {
			if (payload instanceof ArrayBuffer) {
				wsRef.current.send(payload);
			} else {
				wsRef.current.send(JSON.stringify(payload));
			}
		}
	};

	const readyState = wsRef.current?.readyState ?? WebSocket.CLOSED;
	return { send, readyState };
}
