import { useEffect, useRef } from "react";
import { useAudioDialogue } from "@renderer/hooks/ai/useAudioDialogue";
import { AvatarState } from "@renderer/contexts/AIContext";

export const useActionPlay = (options: {
	audioFile?: string;
	dialogue?: string;
	setDialogue?: (text: string) => void;
	setAvatarState?: (value: AvatarState) => void;
	shouldActivate?: boolean;
	avatarState?: AvatarState;
}): { isAudioPlaying: boolean } => {
	const { playAudioDialogue, isAudioPlaying } = useAudioDialogue(options.audioFile || "");
	const hasPlayed = useRef(false);

	const {
		shouldActivate = false,
		dialogue,
		setDialogue,
		setAvatarState,
		avatarState = "idle"
	} = options;

	useEffect(() => {
		if (shouldActivate && !hasPlayed.current) {
			if (setAvatarState) setAvatarState(avatarState);
			if (dialogue && setDialogue) playAudioDialogue(dialogue, setDialogue);
			hasPlayed.current = true;
		}
	}, [shouldActivate, playAudioDialogue, dialogue, avatarState, setDialogue, setAvatarState]);

	return { isAudioPlaying };
};
