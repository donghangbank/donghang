import { useEffect, useRef } from "react";

export function useVideoStream(): {
	videoRef: React.RefObject<HTMLVideoElement>;
	canvasRef: React.RefObject<HTMLCanvasElement>;
} {
	const videoRef = useRef<HTMLVideoElement>(null);
	const canvasRef = useRef<HTMLCanvasElement>(null);

	useEffect(() => {
		navigator.mediaDevices.getUserMedia({ video: true, audio: true }).then((stream) => {
			if (videoRef.current) videoRef.current.srcObject = stream;
		});
	}, []);

	return { videoRef, canvasRef };
}
