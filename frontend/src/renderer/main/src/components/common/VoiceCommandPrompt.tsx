import propTypes from "prop-types";

interface VoiceCommandPromptProps {
	text: string;
}

export const VoiceCommandPrompt = ({ text }: VoiceCommandPromptProps): JSX.Element => {
	return (
		<span className="text-6xl inline-flex items-center justify-center px-10 py-5 bg-cloudyBlue rounded-[32px]">
			{text}
		</span>
	);
};

VoiceCommandPrompt.propTypes = {
	text: propTypes.string.isRequired
};

export default VoiceCommandPrompt;
