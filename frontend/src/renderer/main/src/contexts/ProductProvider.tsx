import { useCallback, useState } from "react";
import { ProductContext } from "./ProductContext";

export const ProductProvider = ({ children }: { children: React.ReactNode }): JSX.Element => {
	const [memberId, setMemberId] = useState(0);
	const [accountProductId, setAccountProductId] = useState(0);
	const [password, setPassword] = useState("");
	const [withdrawalAccountNumber, setWithdrawalAccountNumber] = useState("");
	const [payoutAccountNumber, setPayoutAccountNumber] = useState("");
	const [amount, setAmount] = useState("");
	const [day, setDay] = useState("");
	const [minAmount, setMinAmount] = useState(0);
	const [maxAmount, setMaxAmount] = useState(0);
	const [productName, setProductName] = useState("");
	const [accountNumber, setAccountNumber] = useState("");
	const [accountBalance, setAccountBalance] = useState(0);
	const [interestRate, setInterestRate] = useState(0);
	const [accountExpiryDate, setAccountExpiryDate] = useState("");
	const [nextInstallmentScheduleDate, setNextInstallmentScheduleDate] = useState("");

	const resetAll = useCallback(() => {
		setMemberId(0);
		setAccountProductId(0);
		setPassword("");
		setWithdrawalAccountNumber("");
		setPayoutAccountNumber("");
		setAmount("");
		setDay("");
		setMinAmount(0);
		setMaxAmount(0);
		setProductName("");
		setAccountNumber("");
		setAccountBalance(0);
		setInterestRate(0);
		setAccountExpiryDate("");
		setNextInstallmentScheduleDate("");
	}, []);

	return (
		<ProductContext.Provider
			value={{
				memberId,
				accountProductId,
				password,
				withdrawalAccountNumber,
				payoutAccountNumber,
				amount,
				day,
				minAmount,
				maxAmount,
				productName,
				accountNumber,
				accountBalance,
				interestRate,
				accountExpiryDate,
				nextInstallmentScheduleDate,
				setMemberId,
				setAccountProductId,
				setPassword,
				setWithdrawalAccountNumber,
				setPayoutAccountNumber,
				setAmount,
				setDay,
				setMinAmount,
				setMaxAmount,
				setProductName,
				setAccountNumber,
				setAccountBalance,
				setInterestRate,
				setAccountExpiryDate,
				setNextInstallmentScheduleDate,
				resetAll
			}}
		>
			{children}
		</ProductContext.Provider>
	);
};
