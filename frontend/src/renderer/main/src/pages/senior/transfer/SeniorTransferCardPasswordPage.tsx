import CardPassword from "@renderer/components/common/senior/CardPassword";

export default function SeniorTransferCardPasswordPage(): JSX.Element {
	return (
		<CardPassword
			cardNumber="9999999999999999"
			prev="/senior/transfer/card/input"
			link="/senior/transfer/info/account"
		/>
	);
}
