import JobChoiceCheck from "@renderer/components/common/senior/JobChoiceCheck";
import help_deposit_account from "@renderer/assets/audios/help_deposit_account.mp3?url";

export default function SeniorDepositProductCheckPage(): JSX.Element {
	return (
		<>
			<JobChoiceCheck
				job="예금 계좌 개설"
				audioFile={help_deposit_account}
				link="/senior/depositproducts/recommendation/question"
			/>
		</>
	);
}
