import { createContext } from "react";
import type { Dispatch, SetStateAction } from "react";

export interface ProductContextProps {
	memberId: number;
	accountProductId: number;
	password: string;
	withdrawalAccountNumber: string;
	payoutAccountNumber: string;
	amount: string;
	day: number;
	minAmount: number;
	maxAmount: number;
	productName: string;
	accountNumber: string;
	accountBalance: number;
	interestRate: number;
	accountExpiryDate: string;
	setMemberId: Dispatch<SetStateAction<number>>;
	setAccountProductId: Dispatch<SetStateAction<number>>;
	setPassword: Dispatch<SetStateAction<string>>;
	setWithdrawalAccountNumber: Dispatch<SetStateAction<string>>;
	setPayoutAccountNumber: Dispatch<SetStateAction<string>>;
	setAmount: Dispatch<SetStateAction<string>>;
	setDay: Dispatch<SetStateAction<number>>;
	setMinAmount: Dispatch<SetStateAction<number>>;
	setMaxAmount: Dispatch<SetStateAction<number>>;
	setProductName: Dispatch<SetStateAction<string>>;
	setAccountNumber: Dispatch<SetStateAction<string>>;
	setAccountBalance: Dispatch<SetStateAction<number>>;
	setInterestRate: Dispatch<SetStateAction<number>>;
	setAccountExpiryDate: Dispatch<SetStateAction<string>>;
	resetAll: () => void;
}

export const ProductContext = createContext<ProductContextProps>({
	memberId: 0,
	accountProductId: 0,
	password: "",
	withdrawalAccountNumber: "",
	payoutAccountNumber: "",
	amount: "",
	day: 0,
	minAmount: 0,
	maxAmount: 0,
	productName: "",
	accountNumber: "",
	accountBalance: 0,
	interestRate: 0,
	accountExpiryDate: "",
	setMemberId: () => {},
	setAccountProductId: () => {},
	setPassword: () => {},
	setWithdrawalAccountNumber: () => {},
	setPayoutAccountNumber: () => {},
	setAmount: () => {},
	setDay: () => {},
	setMinAmount: () => {},
	setMaxAmount: () => {},
	setProductName: () => {},
	setAccountNumber: () => {},
	setAccountBalance: () => {},
	setInterestRate: () => {},
	setAccountExpiryDate: () => {},
	resetAll: () => {}
});
