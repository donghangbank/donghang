import AccountPassword from "@renderer/components/common/general/AccountPassword";
import { InputContext } from "@renderer/contexts/InputContext";
import { useContext } from "react";

export const TransferAccountPasswordPage = (): JSX.Element => {
	const { password, setPassword, sendingAccountNumber, setSendingAccountNumber } =
		useContext(InputContext);

	return (
		<AccountPassword
			password={password}
			setPassword={setPassword}
			onSuccess={() => {
				setSendingAccountNumber(sendingAccountNumber);
			}}
			successNavigatePath="/general/transfer/info/account"
		/>
	);
};

export default TransferAccountPasswordPage;
