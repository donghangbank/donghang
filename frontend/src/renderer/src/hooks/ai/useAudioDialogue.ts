import { useState, useEffect, useMemo, useCallback } from "react";

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useAudioDialogue = (audioFile: string): any => {
	const [isAudioPlaying, setIsAudioPlaying] = useState(false);
	const audio = useMemo(() => new Audio(audioFile), [audioFile]);

	const play = useCallback(
		(dialogue: string, setDialogue: (text: string) => void) => {
			setDialogue(dialogue);
			audio.currentTime = 0;
			audio.play();
		},
		[audio]
	);

	useEffect(() => {
		const handlePlay = (): void => setIsAudioPlaying(true);
		const handleEnd = (): void => setIsAudioPlaying(false);

		audio.addEventListener("play", handlePlay);
		audio.addEventListener("ended", handleEnd);

		return (): void => {
			audio.removeEventListener("play", handlePlay);
			audio.removeEventListener("ended", handleEnd);
			audio.pause();
		};
	}, [audio]);

	return { isAudioPlaying, playAudioDialogue: play };
};
