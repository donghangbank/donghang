import React, { useCallback, useState } from "react";
import { SpecSheetContext } from "./SpecSheetContext";

export const SpecSheetProvider = ({ children }: { children: React.ReactNode }): JSX.Element => {
	const [transactionTime, setTransactionTime] = useState<string>("");
	const [recipientName, setRecipientName] = useState<string>("");
	const [receivingAccountNumber, setReceivingAccountNumber] = useState<string>("");
	const [amount, setAmount] = useState<number>(0);
	const [sendingAccountBalance, setSendingAccountBalance] = useState<number>(0);
	const [userId, setUserId] = useState<number>(0);
	const [password, setPassword] = useState<string>("");

	const resetSpecSheet = useCallback(() => {
		setAmount(0);
		setReceivingAccountNumber("");
		setRecipientName("");
		setSendingAccountBalance(0);
		setTransactionTime("");
	}, []);

	return (
		<SpecSheetContext.Provider
			value={{
				transactionTime,
				recipientName,
				receivingAccountNumber,
				amount,
				sendingAccountBalance,
				userId,
				password,
				setTransactionTime,
				setRecipientName,
				setReceivingAccountNumber,
				setAmount,
				setSendingAccountBalance,
				resetSpecSheet,
				setUserId,
				setPassword
			}}
		>
			{children}
		</SpecSheetContext.Provider>
	);
};
