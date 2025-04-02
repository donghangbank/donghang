import { createContext, Dispatch, SetStateAction } from "react";

export interface InputContextProps {
	password: string;
	account: string;
	amount: string;
	residentNumber: string;
	confirmTrigger: number;
	disabled: boolean;
	setPassword: Dispatch<SetStateAction<string>>;
	setAccount: Dispatch<SetStateAction<string>>;
	setAmount: Dispatch<SetStateAction<string>>;
	setResidentNumber: Dispatch<SetStateAction<string>>;
	setConfirmTrigger: Dispatch<SetStateAction<number>>;
	setDisabled: Dispatch<SetStateAction<boolean>>;
}

export const InputContext = createContext<InputContextProps>({
	password: "",
	account: "",
	amount: "",
	residentNumber: "",
	confirmTrigger: 0,
	disabled: false,
	setPassword: () => {},
	setAccount: () => {},
	setAmount: () => {},
	setResidentNumber: () => {},
	setConfirmTrigger: () => {},
	setDisabled: () => {}
});
