import { createContext } from "react";
import { AnimationType } from "@renderer/components/banker/AvatarController";

export type AvatarState = AnimationType;
export type Construction = (typeof CONSTRUCTION_VALUES)[number];
export const CONSTRUCTION_VALUES = [
	"etc",
	"입금",
	"출금",
	"이체",
	"긍정",
	"부정",
	"홈",
	"계좌개설",
	"계좌조회",
	"보이스피싱",
	"통장선택",
	"계좌선택",
	"카드선택",
	"예금통장개설"
] as const;

export const AIContext = createContext<{
	avatarState: AvatarState;
	setAvatarState: (value: AvatarState) => void;
	dialogue: string;
	setDialogue: (value: string) => void;
	construction: Construction;
	setConstruction: (value: Construction) => void;
	audioStop: boolean;
	setAudioStop: (value: boolean) => void;
	recommendAccount: string;
	setRecommendAccount: (value: string) => void;
	resetAIContext: () => void;
}>({
	avatarState: "idle",
	setAvatarState: () => {},
	dialogue: "",
	setDialogue: () => {},
	construction: "etc",
	setConstruction: () => {},
	audioStop: false,
	setAudioStop: () => {},
	recommendAccount: "",
	setRecommendAccount: () => {},
	resetAIContext: () => {}
});
