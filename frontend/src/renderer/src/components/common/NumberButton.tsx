import PropTypes from "prop-types";

interface NumberButtonProps {
	text: string;
	bgColor: string;
	isSquare: boolean;
}

export const NumberButton: React.FC<NumberButtonProps> = ({ text, bgColor, isSquare }) => {
	return (
		<button
			type="button"
			className={`text-7xl font-bold text-white w-full ${isSquare ? "aspect-square" : "h-full"} rounded-2xl ${bgColor}`}
		>
			{text}
		</button>
	);
};

NumberButton.propTypes = {
	text: PropTypes.string.isRequired,
	bgColor: PropTypes.string.isRequired,
	isSquare: PropTypes.bool.isRequired
};

export default NumberButton;
