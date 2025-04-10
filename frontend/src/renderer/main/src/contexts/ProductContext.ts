import { createContext } from "react";
import type { Dispatch, SetStateAction } from "react";

export interface ProductContextProps {
	memberId: number;
	accountProductId: number;
	password: string;
	withdrawalAccountNumber: string;
	payoutAccountNumber: string;
	amount: string;
	day: string;
	minAmount: number;
	maxAmount: number;
	productName: string;
	productDescription: string;
	period: number;
	accountNumber: string;
	accountBalance: number;
	interestRate: number;
	accountExpiryDate: string;
	nextInstallmentScheduleDate: string;
	setMemberId: Dispatch<SetStateAction<number>>;
	setAccountProductId: Dispatch<SetStateAction<number>>;
	setPassword: Dispatch<SetStateAction<string>>;
	setWithdrawalAccountNumber: Dispatch<SetStateAction<string>>;
	setPayoutAccountNumber: Dispatch<SetStateAction<string>>;
	setAmount: Dispatch<SetStateAction<string>>;
	setDay: Dispatch<SetStateAction<string>>;
	setMinAmount: Dispatch<SetStateAction<number>>;
	setMaxAmount: Dispatch<SetStateAction<number>>;
	setProductName: Dispatch<SetStateAction<string>>;
	setProductDescription: Dispatch<SetStateAction<string>>;
	setPeriod: Dispatch<SetStateAction<number>>;
	setAccountNumber: Dispatch<SetStateAction<string>>;
	setAccountBalance: Dispatch<SetStateAction<number>>;
	setInterestRate: Dispatch<SetStateAction<number>>;
	setAccountExpiryDate: Dispatch<SetStateAction<string>>;
	setNextInstallmentScheduleDate: Dispatch<SetStateAction<string>>;
	resetAll: () => void;
}

export const ProductContext = createContext<ProductContextProps>({
	memberId: 0,
	accountProductId: 0,
	password: "",
	withdrawalAccountNumber: "",
	payoutAccountNumber: "",
	amount: "",
	day: "",
	minAmount: 0,
	maxAmount: 0,
	productName: "",
	productDescription: "",
	period: 0,
	accountNumber: "",
	accountBalance: 0,
	interestRate: 0,
	accountExpiryDate: "",
	nextInstallmentScheduleDate: "",
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
	setProductDescription: () => {},
	setPeriod: () => {},
	setAccountNumber: () => {},
	setAccountBalance: () => {},
	setInterestRate: () => {},
	setAccountExpiryDate: () => {},
	setNextInstallmentScheduleDate: () => {},
	resetAll: () => {}
});
