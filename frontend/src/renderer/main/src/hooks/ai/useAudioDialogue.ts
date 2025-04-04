import { useState, useRef, useEffect } from "react";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useAudioDialogue = (audioFile: string): any => {
	const audioRef = useRef(new Audio(audioFile));
	const [isAudioPlaying, setIsAudioPlaying] = useState(false);
	const isPlayingRef = useRef(false);

	const playAudioDialogue = async (
		dialogue: string,
		setDialogue: (text: string) => void
	): Promise<void> => {
		const audio = audioRef.current;

		if (!audio.paused) {
			audio.pause();
			audio.currentTime = 0;
		}

		try {
			setDialogue(dialogue);
			setIsAudioPlaying(true);
			isPlayingRef.current = true;
			await audio.play();
		} catch (error) {
			console.error("Audio playback failed:", error);
		} finally {
			audio.onended = (): void => {
				setTimeout(() => {
					setIsAudioPlaying(false);
					isPlayingRef.current = false;
				}, 2000);
			};
			audio.onerror = (): void => {
				setIsAudioPlaying(false);
				isPlayingRef.current = false;
			};
		}
	};

	useEffect(() => {
		const audio = audioRef.current;

		return (): void => {
			if (!isPlayingRef.current) {
				audio.pause();
				audio.currentTime = 0;
			}
		};
	}, [audioFile]);

	if (!audioFile) {
		return {
			playAudioDialogue: () => Promise.resolve(),
			isAudioPlaying: false
		};
	}

	return { playAudioDialogue, isAudioPlaying };
};
