import PropTypes from "prop-types";

interface NumberButtonProps {
	text: string;
	bgColor: string;
	isSquare: boolean;
	onClick?: () => void;
}

export const NumberButton = ({
	text,
	bgColor,
	isSquare,
	onClick
}: NumberButtonProps): JSX.Element => {
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
