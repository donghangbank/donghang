import SpecSheet from "@renderer/components/common/senior/SpecSheet";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContext } from "react";
import success_deposit from "@renderer/assets/audios/success_deposit.mp3?url";

export default function SeniorDepositProductInfoSpecSheetPage(): JSX.Element {
	const { productName, accountBalance, accountNumber, interestRate, accountExpiryDate } =
		useContext(ProductContext);

	useActionPlay({
		audioFile: success_deposit,
		dialogue: "예금 통장 개설 완료 됐습니다!",
		shouldActivate: true,
		avatarState: "idle"
	});

	const sections = [
		{ label: "상품명", value: productName, formatType: "default" as const },
		{ label: "잔액", value: accountBalance, formatType: "amount" as const },
		{ label: "계좌 번호", value: accountNumber, formatType: "account" as const },
		{ label: "이자율", value: `${interestRate} %`, formatType: "default" as const },
		{ label: `만기일`, value: accountExpiryDate, formatType: "default" as const }
	];
	return (
		<>
			<SpecSheet audioFile={""} sections={sections} />
		</>
	);
}
