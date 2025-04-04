import { motion } from "framer-motion";

interface InputPanelProps {
	inputValue: string;
	format: (value: string) => string;
	hasError?: boolean;
}

export const InputPanel = ({
	inputValue,
	format,
	hasError = false
}: InputPanelProps): JSX.Element => {
	return (
		<motion.div
			className={`${
				hasError ? "bg-red" : "bg-blue"
			} text-white shadow-custom flex items-center justify-center rounded-3xl p-8 text-7xl font-bold h-full w-full`}
			animate={hasError ? { y: [0, -5, 5, -3, 3, -2, 2, 0] } : { y: 0 }}
			transition={{ duration: 0.4 }}
		>
			<span>{format(inputValue)}</span>
		</motion.div>
	);
};

export default InputPanel;
