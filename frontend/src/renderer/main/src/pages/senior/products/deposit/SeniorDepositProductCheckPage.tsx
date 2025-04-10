import JobChoiceCheck from "@renderer/components/common/senior/JobChoiceCheck";

export default function SeniorDepositProductCheckPage(): JSX.Element {
	return (
		<>
			<JobChoiceCheck
				job="예금 계좌 개설"
				audioFile=""
				link="/senior/depositproducts/recommendation"
			/>
		</>
	);
}
