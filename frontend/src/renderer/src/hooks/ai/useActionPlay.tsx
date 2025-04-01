import { useEffect, useRef } from "react";
import { useAudioDialogue } from "@renderer/hooks/ai/useAudioDialogue";
import { AvatarState } from "@renderer/contexts/AIContext";

export const useActionPlay = (options: {
	audioFile: string;
	dialogue: string;
	setDialogue: (text: string) => void;
	setAvatarState: (value: AvatarState) => void;
	shouldActivate: boolean;
	avatarState: AvatarState;
}): { isAudioPlaying } => {
	const { playAudioDialogue, isAudioPlaying } = useAudioDialogue(options.audioFile);
	const hasPlayed = useRef(false);

	const { shouldActivate, dialogue, setDialogue, setAvatarState, avatarState } = options;

	useEffect(() => {
		if (shouldActivate && !hasPlayed.current) {
			setAvatarState(avatarState);
			playAudioDialogue(dialogue, setDialogue);
			hasPlayed.current = true;
		}
	}, [shouldActivate, playAudioDialogue, dialogue, avatarState, setDialogue, setAvatarState]);

	return { isAudioPlaying };
};
