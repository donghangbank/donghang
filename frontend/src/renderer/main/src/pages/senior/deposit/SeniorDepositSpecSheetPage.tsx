import SpecSheet from "@renderer/components/common/senior/SpecSheet";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import { useContext } from "react";

export default function SeniorDepositSpecSheetPage(): JSX.Element {
	const { amount, recipientName, receivingAccountNumber, sendingAccountBalance } =
		useContext(SpecSheetContext);

	const sections = [
		{ label: "계좌주", value: recipientName },
		{ label: "계좌", value: receivingAccountNumber, formatType: "account" as const },
		{ label: "잔액", value: sendingAccountBalance, formatType: "amount" as const },
		{ label: "입금 금액", value: amount, formatType: "amount" as const }
	];

	return <SpecSheet sections={sections} />;
}
