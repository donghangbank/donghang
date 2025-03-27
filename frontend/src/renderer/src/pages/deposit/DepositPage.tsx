import AICanvas from "@renderer/components/banker/AICanvas";
import VoiceCommandPanel from "@renderer/components/common/VoiceCommandPanel";

export const DepositPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "카드", link: "/deposit/card" },
		{ prompt: "통장", link: "/deposit/bankbook" },
		{ prompt: "계좌번호", link: "/deposit/account" }
	];

	return (
		<div className="flex h-full">
			<div className="h-full" style={{ width: "33.33vw" }}>
				<AICanvas />
			</div>
			<div className="h-full" style={{ width: "66.67vw" }}>
				<VoiceCommandPanel title="무엇을 가져 오셨나요?" prompts={prompts} />
			</div>
		</div>
	);
};

export default DepositPage;
