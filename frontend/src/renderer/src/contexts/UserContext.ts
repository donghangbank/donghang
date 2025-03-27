import { createContext } from "react";

export const UserContext = createContext<{
	isElderly: boolean;
	setIsElderly: (value: boolean) => void;
	isUsingPhone: boolean;
	setIsUsingPhone: (value: boolean) => void;
	isTalking: boolean;
	setIsTalking: (value: boolean) => void;
	userMsg: string;
	setUserMsg: (value: string) => void;
}>({
	isElderly: false,
	setIsElderly: () => {},
	isUsingPhone: false,
	setIsUsingPhone: () => {},
	isTalking: false,
	setIsTalking: () => {},
	userMsg: "",
	setUserMsg: () => {}
});
