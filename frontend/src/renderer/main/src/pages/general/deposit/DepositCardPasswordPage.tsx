import CardPassword from "@renderer/components/common/general/CardPassword";
import { InputContext } from "@renderer/contexts/InputContext";
import { useContext } from "react";

export const DepositCardPasswordPage = (): JSX.Element => {
	const { password, setPassword, setReceivingAccountNumber } = useContext(InputContext);

	return (
		<CardPassword
			password={password}
			setPassword={setPassword}
			onSuccess={(data) => {
				setReceivingAccountNumber(data?.fullAccountNumber ?? "");
			}}
			successNavigatePath="/general/deposit/payment"
		/>
	);
};

export default DepositCardPasswordPage;
