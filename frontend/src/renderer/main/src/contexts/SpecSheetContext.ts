import { createContext, Dispatch, SetStateAction } from "react";

export interface SpecSheetContextProps {
	transactionTime: string;
	recipientName: string;
	receivingAccountNumber: string;
	amount: number;
	sendingAccountBalance: number;
	setTransactionTime: Dispatch<SetStateAction<string>>;
	setRecipientName: Dispatch<SetStateAction<string>>;
	setReceivingAccountNumber: Dispatch<SetStateAction<string>>;
	setAmount: Dispatch<SetStateAction<number>>;
	setSendingAccountBalance: Dispatch<SetStateAction<number>>;
	resetSpecSheet: () => void;
}

export const SpecSheetContext = createContext<SpecSheetContextProps>({
	transactionTime: "",
	recipientName: "",
	receivingAccountNumber: "",
	amount: 0,
	sendingAccountBalance: 0,
	setTransactionTime: () => {},
	setRecipientName: () => {},
	setReceivingAccountNumber: () => {},
	setAmount: () => {},
	setSendingAccountBalance: () => {},
	resetSpecSheet: () => {}
});
