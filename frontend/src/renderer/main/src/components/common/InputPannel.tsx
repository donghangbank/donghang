import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

interface InputPanelProps {
	inputValue: string;
	mainLabel: string;
	subLabel?: string;
	format: (value: string) => string;
	isCount?: boolean;
	onResetTimer?: () => void;
}

export const InputPanel = ({
	inputValue,
	mainLabel,
	format,
	isCount = true,
	onResetTimer
}: InputPanelProps): JSX.Element => {
	const navigate = useNavigate();
	const [timeLeft, setTimeLeft] = useState(60);

	useEffect(() => {
		if (onResetTimer) {
			setTimeLeft(60);
		}
	}, [onResetTimer]);

	useEffect((): void | (() => void) => {
		const interval = setInterval(() => {
			setTimeLeft((prevTime) => {
				if (prevTime <= 1) {
					clearInterval(interval);
					navigate("/general/final");
					return 0;
				}
				return prevTime - 1;
			});
		}, 1000);
		return () => clearInterval(interval);
	}, [navigate]);

	const minutes = Math.floor(timeLeft / 60);
	const seconds = timeLeft % 60;
	const formattedTime = `${minutes}:${seconds.toString().padStart(2, "0")}`;

	return (
		<div className="bg-white shadow-custom flex flex-col rounded-3xl p-2.5 font-bold">
			<div className="pr-4 pb-3 pl-4 pt-1.5 rounded-3xl flex text-3xl justify-between text-blue">
				<span className="">{mainLabel}</span>
				{isCount && (
					<span className={`${timeLeft < 10 ? "text-red shake" : "text-blue"}`}>
						{formattedTime}
					</span>
				)}
			</div>
			<div className="flex flex-col justify-center items-right rounded-3xl bg-cloudyBlue">
				<input
					className="flex-1 p-2.5 rounded-3xl text-4xl bg-cloudyBlue text-right px-2.5 focus:border-none"
					value={format(inputValue)}
					readOnly
				/>
			</div>
		</div>
	);
};

export default InputPanel;
