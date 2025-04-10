import { ProductContext } from "@renderer/contexts/ProductContext";
import { useContext } from "react";
import CardPassword from "@renderer/components/common/general/CardPassword";

export const InstallmentProductCardPasswordPage = (): JSX.Element => {
	const { setWithdrawalAccountNumber, setPayoutAccountNumber, password, setPassword, setMemberId } =
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
			successNavigatePath="/general/installmentproducts/info/amount"
		/>
	);
};

export default InstallmentProductCardPasswordPage;
