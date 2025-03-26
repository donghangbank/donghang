import { Link } from "react-router-dom";
import { VoiceButton } from "./VoiceButton";
import VoiceCommandPrompt from "./VoiceCommandPrompt";
import PropTypes from "prop-types";

interface VoiceCommandPanelProps {
	title: string;
	prompts: string[];
}

export const VoiceCommandPanel = ({ title, prompts }: VoiceCommandPanelProps): JSX.Element => {
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

			<div className="flex justify-center gap-5">
				<Link to={"/deposit"}>
					<button type="button" className="text-8xl bg-blue rounded-3xl text-white p-5">
						입금하기
					</button>
				</Link>
				<Link to={"/deposit/confirm"}>
					<button type="button" className="text-8xl bg-blue rounded-3xl text-white p-5">
						내용맞음?
					</button>
				</Link>
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
