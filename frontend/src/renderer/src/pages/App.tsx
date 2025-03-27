import AICanvas from "@renderer/components/banker/AICanvas";
import VoiceCommandPanel from "@renderer/components/common/VoiceCommandPanel";

function App(): JSX.Element {
	return (
		<div className="flex h-full">
			<div className="h-full" style={{ width: "33.33vw" }}>
				<AICanvas />
			</div>
			<div className="h-full" style={{ width: "66.67vw" }}>
				<VoiceCommandPanel
					title="원하시는 업무를 말씀해 주세요"
					prompts={["돈을 넣고 싶어", "돈을 꺼내고 싶어", "내가 손녀한테 돈을 보내고 싶어"]}
				/>
			</div>
		</div>
	);
}

export default App;
