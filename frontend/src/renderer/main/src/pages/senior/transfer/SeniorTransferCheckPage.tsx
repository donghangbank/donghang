import transfer_check from "@renderer/assets/audios/transfer_check.mp3?url";
import JobChoiceCheck from "@renderer/components/common/senior/JobChoiceCheck";

export default function SeniorTransferCheckPage(): JSX.Element {
	return (
		<>
			<JobChoiceCheck
				job="이체"
				audioFile={transfer_check}
				prev="/senior"
				link="/senior/transfer/warning/scam"
			/>
		</>
	);
}
