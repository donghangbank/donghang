import { createContext } from "react";

export type AvatarState = "idle" | "walk" | "bow";
export type Construction = "etc" | "입금" | "출금" | "이체";

export const AIContext = createContext<{
	avatarState: AvatarState;
	setAvatarState: (value: AvatarState) => void;
	dialogue: string;
	setDialogue: (value: string) => void;
	construction: Construction;
	setConstruction: (value: Construction) => void;
}>({
	avatarState: "idle",
	setAvatarState: () => {},
	dialogue: "",
	setDialogue: () => {},
	construction: "etc",
	setConstruction: () => {}
});
