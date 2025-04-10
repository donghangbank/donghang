import AccountPassword from "@renderer/components/common/general/AccountPassword";
import { InputContext } from "@renderer/contexts/InputContext";
import { useContext } from "react";

export const InquiryBalanceAccountPasswordPage = (): JSX.Element => {
	const { password, setPassword, sendingAccountNumber, setReceivingAccountNumber } =
		useContext(InputContext);

	return (
		<AccountPassword
			password={password}
			setPassword={setPassword}
			onSuccess={() => {
				setReceivingAccountNumber(sendingAccountNumber);
			}}
			successNavigatePath="/general/balance/specsheet"
		/>
	);
};

export default InquiryBalanceAccountPasswordPage;
