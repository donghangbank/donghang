import { createContext, Dispatch, SetStateAction } from "react";

export interface SpecSheetContextProps {
	transactionTime: string;
	recipientName: string;
	receivingAccountNumber: string;
	amount: number;
	sendingAccountBalance: number;
	userId: number;
	password: string;
	setTransactionTime: Dispatch<SetStateAction<string>>;
	setRecipientName: Dispatch<SetStateAction<string>>;
	setReceivingAccountNumber: Dispatch<SetStateAction<string>>;
	setAmount: Dispatch<SetStateAction<number>>;
	setSendingAccountBalance: Dispatch<SetStateAction<number>>;
	setUserId: Dispatch<SetStateAction<number>>;
	setPassword: Dispatch<SetStateAction<string>>;
	resetSpecSheet: () => void;
}

export const SpecSheetContext = createContext<SpecSheetContextProps>({
	transactionTime: "",
	recipientName: "",
	receivingAccountNumber: "",
	amount: 0,
	sendingAccountBalance: 0,
	userId: 0,
	password: "",
	setTransactionTime: () => {},
	setRecipientName: () => {},
	setReceivingAccountNumber: () => {},
	setAmount: () => {},
	setSendingAccountBalance: () => {},
	setUserId: () => {},
	setPassword: () => {},
	resetSpecSheet: () => {}
});
