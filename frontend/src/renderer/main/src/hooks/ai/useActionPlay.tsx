import { useState, useRef, useEffect, useContext, useCallback } from "react";
import { AvatarState, AIContext } from "@renderer/contexts/AIContext";

interface UseActionPlayOptions {
	audioFile?: string;
	dialogue?: string;
	shouldActivate?: boolean;
	avatarState?: AvatarState;
	animationDelay?: number;
	onComplete?: () => void;
}

export const useActionPlay = ({
	audioFile = "",
	dialogue = "",
	shouldActivate = false,
	avatarState = "idle",
	animationDelay = 0,
	onComplete
}: UseActionPlayOptions): { isAudioPlaying: boolean } => {
	const { setAvatarState, setDialogue } = useContext(AIContext);
	const audioRef = useRef<HTMLAudioElement | null>(audioFile ? new Audio(audioFile) : null);
	const [isAudioPlaying, setIsAudioPlaying] = useState(false);
	const isPlayingRef = useRef(false);
	const hasPlayed = useRef(false);
	const prevAudioFile = useRef<string>(audioFile);

	// Handle audio file changes and interrupt current playback
	useEffect(() => {
		if (prevAudioFile.current !== audioFile) {
			if (audioRef.current) {
				// Immediately stop current audio if playing
				if (!audioRef.current.paused) {
					audioRef.current.pause();
					audioRef.current.currentTime = 0;
					setIsAudioPlaying(false);
					isPlayingRef.current = false;
				}
				audioRef.current = null;
			}
			if (audioFile) {
				audioRef.current = new Audio(audioFile);
			}
			prevAudioFile.current = audioFile;
			hasPlayed.current = false; // Reset so new audio can play
		}
	}, [audioFile]);

	// Play audio function
	const playAudio = useCallback(async (): Promise<void> => {
		if (!audioFile || !audioRef.current) return;

		const audio = audioRef.current;

		try {
			// If already playing, stop it immediately
			if (!audio.paused) {
				audio.pause();
				audio.currentTime = 0;
			}

			await audio.play();
			setIsAudioPlaying(true);
			isPlayingRef.current = true;
		} catch (error) {
			console.error("Audio playback failed:", error);
			setIsAudioPlaying(false);
			isPlayingRef.current = false;
		}
	}, [audioFile]);

	// Handle audio events and cleanup
	useEffect(() => {
		const audio = audioRef.current;
		if (!audio) return;

		const handleEnded = (): void => {
			setTimeout(() => {
				setIsAudioPlaying(false);
				isPlayingRef.current = false;
				if (onComplete) onComplete();
			}, 1000);
		};

		const handleError = (): void => {
			setIsAudioPlaying(false);
			isPlayingRef.current = false;
		};

		audio.addEventListener("ended", handleEnded);
		audio.addEventListener("error", handleError);

		return (): void => {
			audio.removeEventListener("ended", handleEnded);
			audio.removeEventListener("error", handleError);
			// No need to pause here; handled by audioFile change
		};
	}, [audioFile, onComplete]);

	// Trigger playback when shouldActivate is true
	useEffect(() => {
		if (shouldActivate && !hasPlayed.current) {
			if (audioFile) {
				playAudio();
			}

			if (dialogue) setDialogue(dialogue);

			const timer = setTimeout(() => {
				setAvatarState(avatarState);
				hasPlayed.current = true;
				if (!audioFile && onComplete) {
					setTimeout(onComplete, 2000);
				}
			}, animationDelay);

			return (): void => clearTimeout(timer);
		}

		return (): void => {};
	}, [
		shouldActivate,
		audioFile,
		dialogue,
		avatarState,
		animationDelay,
		playAudio,
		setAvatarState,
		setDialogue,
		onComplete
	]);

	// Reset hasPlayed when shouldActivate toggles off
	useEffect(() => {
		if (!shouldActivate) {
			hasPlayed.current = false;
		}
	}, [shouldActivate]);

	return { isAudioPlaying };
};
