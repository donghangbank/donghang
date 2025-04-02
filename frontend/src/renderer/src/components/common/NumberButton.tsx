interface NumberButtonProps {
	text: string;
	bgColor: string;
	isSquare: boolean;
	onClick?: () => void;
	textColor?: string;
}

export const NumberButton = ({
	text,
	bgColor,
	isSquare,
	onClick,
	textColor = "white"
}: NumberButtonProps): JSX.Element => {
	return (
		<button
			type="button"
			className={`text-${textColor} w-full ${isSquare ? "aspect-square" : "h-full"} rounded-xl ${bgColor}`}
			onClick={onClick}
		>
			{text}
		</button>
	);
};

export default NumberButton;
