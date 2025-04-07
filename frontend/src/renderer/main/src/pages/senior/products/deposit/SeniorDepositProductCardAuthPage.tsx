import CardAuth from "@renderer/components/common/senior/CardAuth";

export default function SeniorDepositProductCardAuthPage(): JSX.Element {
	return (
		<CardAuth
			prev="/senior/depositproducts/card/input"
			link="/senior/depositproducts/card/password"
		/>
	);
}
