import CardPassword from "@renderer/components/common/general/CardPassword";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useContext } from "react";

export const DepositProductCardPasswordPage = (): JSX.Element => {
	const { password, setPassword, setWithdrawalAccountNumber, setPayoutAccountNumber, setMemberId } =
		useContext(ProductContext);

	return (
		<CardPassword
			password={password}
			setPassword={setPassword}
			onSuccess={(data) => {
				setWithdrawalAccountNumber(data.fullAccountNumber);
				setPayoutAccountNumber(data.fullAccountNumber);
				setMemberId(data.ownerId);
			}}
			successNavigatePath="/general/depositproducts/info/amount"
		/>
	);
};

export default DepositProductCardPasswordPage;
