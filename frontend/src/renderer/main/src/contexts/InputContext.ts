import { createContext, Dispatch, SetStateAction } from "react";

export interface InputContextProps {
	password: string;
	sendingAccountNumber: string;
	receivingAccountNumber: string;
	amount: string;
	residentNumber: string;
	confirmTrigger: number;
	disabled: boolean;
	setPassword: Dispatch<SetStateAction<string>>;
	setSendingAccountNumber: Dispatch<SetStateAction<string>>;
	setReceivingAccountNumber: Dispatch<SetStateAction<string>>;
	setAmount: Dispatch<SetStateAction<string>>;
	setResidentNumber: Dispatch<SetStateAction<string>>;
	setConfirmTrigger: Dispatch<SetStateAction<number>>;
	setDisabled: Dispatch<SetStateAction<boolean>>;
	resetAll: () => void;
}

export const InputContext = createContext<InputContextProps>({
	password: "",
	sendingAccountNumber: "",
	receivingAccountNumber: "",
	amount: "",
	residentNumber: "",
	confirmTrigger: 0,
	disabled: false,
	setPassword: () => {},
	setSendingAccountNumber: () => {},
	setReceivingAccountNumber: () => {},
	setAmount: () => {},
	setResidentNumber: () => {},
	setConfirmTrigger: () => {},
	setDisabled: () => {},
	resetAll: () => {}
});
