import AudioVisualizer from "./AudioVisualizer";
import VoiceCommandPrompt from "./VoiceCommandPrompt";
import PropTypes from "prop-types";

interface VoiceCommandPanelProps {
	title: string;
	prompts: string[];
}

export const VoiceCommandPanel: React.FC<VoiceCommandPanelProps> = ({ title, prompts }) => {
	const match = title.match(/(.+?)([을를])(.*)/);
	const [blueText, particle, restText] = match ? [match[1], match[2], match[3]] : [title, "", ""];

	return (
		<div className="flex flex-col h-screen justify-between items-center p-10">
			<div className="w-full flex flex-col items-center p-10 gap-10 bg-white rounded-2xl shadow-custom">
				<span className="text-7xl font-bold text-center">
					<span className="text-blue">{blueText}</span>
					{particle}
					{restText}
				</span>

				{prompts.map((text, idx) => (
					<VoiceCommandPrompt key={`${idx}. ${text}`} text={text} />
				))}

				<span className="text-4xl text-[#777777]">안전한 금융거래를 위해 음성이 녹음됩니다.</span>
			</div>

			<div className="inline-flex items-center px-5 py-10 rounded-full bg-purple-linear animate-pulse self-center">
				<AudioVisualizer />
				<span className="ml-3 text-white text-5xl font-bold">듣고 있어요</span>
			</div>
		</div>
	);
};

VoiceCommandPanel.propTypes = {
	title: PropTypes.string.isRequired,
	prompts: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired
};

export default VoiceCommandPanel;
