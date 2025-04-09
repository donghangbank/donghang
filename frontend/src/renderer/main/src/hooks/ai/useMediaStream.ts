import { useEffect, useRef, useState } from "react";

export function useMediaStream(): {
	videoRef: React.RefObject<HTMLVideoElement>;
	canvasRef: React.RefObject<HTMLCanvasElement>;
	stream: MediaStream | null;
} {
	const videoRef = useRef<HTMLVideoElement>(null);
	const canvasRef = useRef<HTMLCanvasElement>(null);
	const [stream, setStream] = useState<MediaStream | null>(null);

	useEffect(() => {
		const startStream = async (): Promise<void> => {
			try {
				const mediaStream = await navigator.mediaDevices.getUserMedia({
					video: true,
					audio: {
						noiseSuppression: true,
						echoCancellation: true,
						autoGainControl: true
					}
				});
				if (videoRef.current) {
					videoRef.current.srcObject = mediaStream;
				}
				setStream(mediaStream);
			} catch (error) {
				console.error("Error starting media stream:", error);
			}
		};
		startStream();

		// Cleanup
		return (): void => {
			stream?.getTracks().forEach((track) => track.stop());
		};
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, []);

	return { videoRef, canvasRef, stream };
}
