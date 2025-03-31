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

	useEffect(() => {
		if (options.shouldActivate && !hasPlayed.current) {
			options.setAvatarState(options.avatarState);
			playAudioDialogue(options.dialogue, options.setDialogue);
			hasPlayed.current = true;
		}
	}, [options, playAudioDialogue]);

	return { isAudioPlaying };
};
