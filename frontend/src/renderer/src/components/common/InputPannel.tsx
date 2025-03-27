import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import PropTypes from "prop-types";

interface InputPanelProps {
	inputValue: string;
	mainLabel: string;
	subLabel: string;
	format: (value: string) => string;
}

export const InputPanel = ({
	inputValue,
	mainLabel,
	subLabel,
	format
}: InputPanelProps): JSX.Element => {
	const navigate = useNavigate();
	const [timeLeft, setTimeLeft] = useState(60);

	useEffect((): void | (() => void) => {
		const interval = setInterval(() => {
			setTimeLeft((prevTime) => {
				if (prevTime <= 1) {
					clearInterval(interval);
					navigate("/");
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
		<div className="flex flex-col h-full">
			<div className="flex flex-col flex-1 justify-between m-10 p-10 bg-white rounded-2xl shadow-custom">
				<div className="text-right flex flex-col justify-center flex-1">
					<span className="text-8xl leading-snug">
						<span className="font-bold">{mainLabel}</span>
						{mainLabel === "금액" ? "을" : "를"} 입력하시고
						<br />
						<span className="text-green font-bold">확인</span>을 눌러주십시오
						{(mainLabel === "비밀번호" || mainLabel === "주민등록번호") && (
							<>
								<br />
								<span className="text-6xl text-red font-bold">* 입력하실 때 주의하세요!</span>
							</>
						)}
					</span>
				</div>
				<div className="flex flex-col p-10 gap-16 bg-background rounded-2xl">
					<div className="flex justify-between">
						<span className="text-8xl text-blue font-bold">{subLabel}</span>
						<span
							className={`text-8xl font-bold ${timeLeft < 10 ? "text-red shake" : "text-blue"}`}
						>
							{formattedTime}
						</span>
					</div>
					<div className="flex flex-col">
						<input
							className="flex-1 text-8xl rounded-2xl border-2 border-black text-right px-2.5 font-bold"
							value={format(inputValue)}
							readOnly
						/>
					</div>
				</div>
			</div>
		</div>
	);
};

InputPanel.propTypes = {
	inputValue: PropTypes.string.isRequired,
	mainLabel: PropTypes.string.isRequired,
	subLabel: PropTypes.string.isRequired,
	format: PropTypes.func.isRequired
};

export default InputPanel;
