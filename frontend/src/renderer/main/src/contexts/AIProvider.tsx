import { useState } from "react";
import { AIContext, AvatarState, Construction } from "./AIContext";

export function AIProvider({ children }: { children: React.ReactNode }): JSX.Element {
	const [avatarState, setAvatarState] = useState<AvatarState>("idle");
	const [dialogue, setDialogue] = useState<string>("");
	const [construction, setConstruction] = useState<Construction>("etc");
	const [audioStop, setAudioStop] = useState<boolean>(false);

	return (
		<AIContext.Provider
			value={{
				avatarState,
				setAvatarState,
				dialogue,
				setDialogue,
				construction,
				setConstruction,
				audioStop,
				setAudioStop
			}}
		>
			{children}
		</AIContext.Provider>
	);
}
