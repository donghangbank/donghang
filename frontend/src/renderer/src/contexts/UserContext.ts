import { createContext } from "react";

export const UserContext = createContext<{
	isElderly: boolean;
	setIsElderly: (value: boolean) => void;
	isUsingPhone: boolean;
	setIsUsingPhone: (value: boolean) => void;
}>({
	isElderly: false,
	setIsElderly: () => {},
	isUsingPhone: false,
	setIsUsingPhone: () => {}
});
