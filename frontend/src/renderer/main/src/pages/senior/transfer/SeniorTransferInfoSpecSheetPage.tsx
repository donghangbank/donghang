import SpecSheet from "@renderer/components/common/senior/SpecSheet";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import { useContext } from "react";

// Example usage with original sections
export default function SeniorTransferInfoSpecSheetPage(): JSX.Element {
	const { amount, recipientName, sendingAccountBalance } = useContext(SpecSheetContext);

	const sections = [
		{ label: "받는 사람", value: recipientName },
		{ label: "잔액", value: sendingAccountBalance, formatType: "amount" as const },
		{ label: `${recipientName} 님께 보낸 돈`, value: amount, formatType: "amount" as const }
	];

	return <SpecSheet sections={sections} />;
}
