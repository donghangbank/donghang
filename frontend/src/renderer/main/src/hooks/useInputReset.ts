import { useContext } from "react";
import { InputContext } from "../contexts/InputContext";

interface UseInputResetReturn {
	resetInputContext: () => void;
}

export const useInputReset = (): UseInputResetReturn => {
	const {
		setPassword,
		setSendingAccountNumber,
		setReceivingAccountNumber,
		setAmount,
		setResidentNumber,
		setConfirmTrigger
	} = useContext(InputContext);

	const resetInputContext = (): void => {
		setPassword("");
		setSendingAccountNumber("");
		setReceivingAccountNumber("");
		setAmount("");
		setResidentNumber("");
		setConfirmTrigger(0);
	};

	return { resetInputContext };
};
