import CardPassword from "@renderer/components/common/general/CardPassword";
import { InputContext } from "@renderer/contexts/InputContext";
import { useContext } from "react";

export const InquiryBalanceCardPasswordPage = (): JSX.Element => {
	const { password, setPassword, setReceivingAccountNumber } = useContext(InputContext);

	return (
		<CardPassword
			password={password}
			setPassword={setPassword}
			onSuccess={(data) => {
				setReceivingAccountNumber(data?.fullAccountNumber ?? "");
			}}
			successNavigatePath="/general/balance/specsheet"
		/>
	);
};

export default InquiryBalanceCardPasswordPage;
