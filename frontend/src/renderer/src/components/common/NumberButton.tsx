import PropTypes from "prop-types";

interface NumberButtonProps {
	text: string;
	bgColor: string;
	isSquare: boolean;
	onClick?: () => void;
}

export const NumberButton: React.FC<NumberButtonProps> = ({ text, bgColor, isSquare, onClick }) => {
	return (
		<button
			type="button"
			className={`text-7xl font-bold text-white w-full ${isSquare ? "aspect-square" : "h-full"} rounded-2xl ${bgColor}`}
			onClick={onClick}
		>
			{text}
		</button>
	);
};

NumberButton.propTypes = {
	text: PropTypes.string.isRequired,
	bgColor: PropTypes.string.isRequired,
	isSquare: PropTypes.bool.isRequired,
	onClick: PropTypes.func
};

export default NumberButton;
