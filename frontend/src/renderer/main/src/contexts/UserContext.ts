import { createContext } from "react";

export const UserContext = createContext<{
	isUserExist: boolean;
	setIsUserExist: (value: boolean) => void;
	isElderly: number;
	setIsElderly: (value: number) => void;
	isUsingPhone: boolean;
	setIsUsingPhone: (value: boolean) => void;
	isTalking: boolean;
	setIsTalking: (value: boolean) => void;
	userMsg: string;
	setUserMsg: (value: string) => void;
	isNotFishing: boolean;
	setIsNotFishing: (value: boolean) => void;
	resetAll: () => void;
}>({
	isUserExist: false,
	setIsUserExist: () => {},
	isElderly: 0,
	setIsElderly: () => {},
	isUsingPhone: false,
	setIsUsingPhone: () => {},
	isTalking: false,
	setIsTalking: () => {},
	userMsg: "",
	setUserMsg: () => {},
	isNotFishing: false,
	setIsNotFishing: () => {},
	resetAll: () => {}
});
