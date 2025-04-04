import VoiceCommandPanel from "@renderer/components/common/VoiceCommandPanel";

export const DepositPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "카드", link: "/general/deposit/card" },
		{ prompt: "통장", link: "/general/deposit/bankbook" },
		{ prompt: "계좌번호", link: "/general/deposit/account" }
	];

	return (
		<div className="flex h-full">
			<div className="h-full">
				<VoiceCommandPanel title="무엇을 가져 오셨나요?" prompts={prompts} />
			</div>
		</div>
	);
};

export default DepositPage;
