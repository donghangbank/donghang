import React, { useCallback, useState } from "react";
import { InputContext } from "./InputContext";

export const InputProvider = ({ children }: { children: React.ReactNode }): JSX.Element => {
	const [password, setPassword] = useState("");
	const [sendingAccountNumber, setSendingAccountNumber] = useState("");
	const [receivingAccountNumber, setReceivingAccountNumber] = useState("");
	const [amount, setAmount] = useState("");
	const [residentNumber, setResidentNumber] = useState("");
	const [confirmTrigger, setConfirmTrigger] = useState(0);
	const [disabled, setDisabled] = useState(false);

	const resetAll = useCallback(() => {
		setPassword("");
		setSendingAccountNumber("");
		setReceivingAccountNumber("");
		setAmount("");
		setResidentNumber("");
		setConfirmTrigger(0);
		setDisabled(false);
		// 모든 상태 초기화
	}, []);

	return (
		<InputContext.Provider
			value={{
				password,
				sendingAccountNumber,
				receivingAccountNumber,
				amount,
				residentNumber,
				confirmTrigger,
				disabled,
				setPassword,
				setSendingAccountNumber,
				setReceivingAccountNumber,
				setAmount,
				setResidentNumber,
				setConfirmTrigger,
				setDisabled,
				resetAll
			}}
		>
			{children}
		</InputContext.Provider>
	);
};
