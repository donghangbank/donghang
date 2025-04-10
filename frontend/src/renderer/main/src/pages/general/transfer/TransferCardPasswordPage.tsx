import CardPassword from "@renderer/components/common/general/CardPassword";
import { InputContext } from "@renderer/contexts/InputContext";
import { useContext } from "react";

export const TransferCardPasswordPage = (): JSX.Element => {
	const { password, setPassword, setSendingAccountNumber } = useContext(InputContext);

	return (
		<CardPassword
			password={password}
			setPassword={setPassword}
			onSuccess={(data) => {
				setSendingAccountNumber(data?.fullAccountNumber ?? "");
			}}
			successNavigatePath="/general/transfer/info/account"
		/>
	);
};

export default TransferCardPasswordPage;
