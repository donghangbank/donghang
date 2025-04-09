import SpecSheet from "@renderer/components/common/senior/SpecSheet";
import TestButton from "@renderer/components/common/senior/TestButton";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useContext } from "react";

export default function SeniorDepositProductInfoSpecSheetPage(): JSX.Element {
	const { productName, accountBalance, accountNumber, interestRate, accountExpiryDate } =
		useContext(ProductContext);

	const sections = [
		{ label: "상품명", value: productName, formatType: "default" as const },
		{ label: "잔액", value: accountBalance, formatType: "amount" as const },
		{ label: "계좌 번호", value: accountNumber, formatType: "account" as const },
		{ label: "이자율", value: `${interestRate} %`, formatType: "default" as const },
		{ label: `만기일`, value: accountExpiryDate, formatType: "datetime" as const }
	];
	return (
		<>
			<SpecSheet sections={sections} />
			<TestButton prevRoute="/senior/depositproducts/info/amount" nextRoute="/senior/final" />
		</>
	);
}
