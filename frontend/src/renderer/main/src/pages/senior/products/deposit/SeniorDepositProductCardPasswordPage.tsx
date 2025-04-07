import CardPassword from "@renderer/components/common/senior/CardPassword";

export default function SeniorDepositProductCardPasswordPage(): JSX.Element {
	return (
		<CardPassword
			prev="/senior/depositproducts/card/auth"
			isSender={true}
			link="/senior/depositproducts/info/amount"
		/>
	);
}
