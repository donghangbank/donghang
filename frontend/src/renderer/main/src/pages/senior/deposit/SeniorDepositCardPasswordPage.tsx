import CardPassword from "@renderer/components/common/senior/CardPassword";

export default function SeniorDepositCardPasswordPage(): JSX.Element {
	return <CardPassword isSender={false} link="/senior/deposit/payment" />;
}
