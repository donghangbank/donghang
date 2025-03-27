import AICanvas from "@renderer/components/banker/AICanvas";
import VoiceCommandPanel from "@renderer/components/common/VoiceCommandPanel";

export const DepositOptionPage = (): JSX.Element => {
	const prompts = [
		{ prompt: "현금", link: "/deposit/cash/input" },
		{ prompt: "수표", link: "/deposit/cash/input" },
		{ prompt: "현금+수표", link: "/deposit/cash/input" }
	];

	return (
		<div className="flex h-full">
			<div className="h-full" style={{ width: "33.33vw" }}>
				<AICanvas />
			</div>
			<div className="h-full" style={{ width: "66.67vw" }}>
				<VoiceCommandPanel title="무엇으로 돈을 넣으시겠습니까?" prompts={prompts} />
			</div>
		</div>
	);
};

export default DepositOptionPage;
