import CardPassword from "@renderer/components/common/senior/CardPassword";

export default function SeniorDepositCardPasswordPage(): JSX.Element {
	return (
		<CardPassword
			prev="/senior/deposit/card/auth"
			isSender={false}
			link="/senior/deposit/payment"
		/>
	);
}
