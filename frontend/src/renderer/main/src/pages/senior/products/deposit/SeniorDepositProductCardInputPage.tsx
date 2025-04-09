import CardInput from "@renderer/components/common/senior/CardInput";

export default function SeniorDepositProductCardInputPage(): JSX.Element {
	return (
		<CardInput
			prev="/senior/depositproducts/warning/card"
			link="/senior/depositproducts/card/auth"
		/>
	);
}
