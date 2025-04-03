import { createContext } from "react";

export const PageContext = createContext<{
	currentJob: string;
	setCurrentJob: (value: string) => void;
}>({
	currentJob: "",
	setCurrentJob: () => {}
});
