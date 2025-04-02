import React, { useState } from "react";
import { InputContext } from "./InputContext";

export const InputProvider = ({ children }: { children: React.ReactNode }): JSX.Element => {
	const [password, setPassword] = useState("");
	const [account, setAccount] = useState("");
	const [amount, setAmount] = useState("");
	const [residentNumber, setResidentNumber] = useState("");
	const [confirmTrigger, setConfirmTrigger] = useState(0);
	const [disabled, setDisabled] = useState(false);

	return (
		<InputContext.Provider
			value={{
				password,
				account,
				amount,
				residentNumber,
				confirmTrigger,
				disabled,
				setPassword,
				setAccount,
				setAmount,
				setResidentNumber,
				setConfirmTrigger,
				setDisabled
			}}
		>
			{children}
		</InputContext.Provider>
	);
};
