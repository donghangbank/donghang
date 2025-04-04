import { Link } from "react-router-dom";
import { VoiceButton } from "./VoiceButton";
import VoiceCommandPrompt from "./VoiceCommandPrompt";
import PropTypes from "prop-types";

interface VoiceCommandPanelProps {
	title: string;
	prompts: { prompt: string; link: string }[];
}

export const VoiceCommandPanel = ({ title, prompts }: VoiceCommandPanelProps): JSX.Element => {
	const match = title.match(/(.+?)([을를])(.*)/);
	const [blueText, particle, restText] = match ? [match[1], match[2], match[3]] : [title, "", ""];

	return (
		<div className="flex flex-col h-full justify-between items-center p-10">
			<div className="w-full flex flex-col items-center p-10 gap-10 bg-white rounded-2xl shadow-custom">
				<span className="text-7xl font-bold text-center">
					<span className="text-blue">{blueText}</span>
					{particle}
					{restText}
				</span>

				{prompts.map((item, idx) => (
					<Link to={item.link} key={`${idx}. ${item.prompt}`}>
						<VoiceCommandPrompt text={item.prompt} />
					</Link>
				))}

				<span className="text-4xl text-[#777777]">안전한 금융거래를 위해 음성이 녹음됩니다.</span>
			</div>

			<VoiceButton />
		</div>
	);
};

VoiceCommandPanel.propTypes = {
	title: PropTypes.string.isRequired,
	prompts: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired
};

export default VoiceCommandPanel;
