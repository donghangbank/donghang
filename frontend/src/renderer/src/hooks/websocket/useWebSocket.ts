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
): { send: (payload: requestMsg) => void } {
	const wsRef = useRef<WebSocket | null>(null);

	useEffect(() => {
		const ws = new WebSocket(url);
		wsRef.current = ws;

		ws.onopen = (): void => options.onOpen?.();
		ws.onmessage = (e): void => {
			try {
				const data = JSON.parse(e.data);
				options.onMessage?.(data);
			} catch (err) {
				console.error("WebSocket JSON parse error", err);
			}
		};
		ws.onerror = (e): void => options.onError?.(e);
		ws.onclose = (): void => options.onClose?.();

		return (): void => ws.close();
	}, [url, options]);

	const send = (payload: requestMsg): void => {
		if (wsRef.current?.readyState === WebSocket.OPEN) {
			wsRef.current.send(JSON.stringify(payload));
		}
	};

	return { send };
}
