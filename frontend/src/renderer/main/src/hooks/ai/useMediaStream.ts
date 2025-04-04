import { useEffect, useRef } from "react";

export function useMediaStream(): {
	videoRef: React.RefObject<HTMLVideoElement>;
	canvasRef: React.RefObject<HTMLCanvasElement>;
	audioRef: React.RefObject<HTMLAudioElement>;
} {
	const videoRef = useRef<HTMLVideoElement>(null);
	const canvasRef = useRef<HTMLCanvasElement>(null);
	const audioRef = useRef<HTMLAudioElement>(null);

	useEffect(() => {
		navigator.mediaDevices.getUserMedia({ video: true, audio: true }).then((stream) => {
			if (videoRef.current) videoRef.current.srcObject = stream;
			if (audioRef.current) audioRef.current.srcObject = stream;
		});
	}, []);

	return { videoRef, canvasRef, audioRef };
}
