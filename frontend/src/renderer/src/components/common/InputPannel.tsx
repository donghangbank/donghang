import PropTypes from "prop-types";

interface InputPanelProps {
	inputValue: string;
	mainLabel: string;
	subLabel: string;
	format: (value: string) => string;
}

export const InputPanel: React.FC<InputPanelProps> = ({
	inputValue,
	mainLabel,
	subLabel,
	format
}) => {
	return (
		<div className="flex flex-col h-screen">
			<div className="flex flex-col flex-1 justify-between m-10 p-10 bg-white rounded-2xl shadow-custom">
				<div className="text-right flex flex-col justify-center flex-1">
					<span className="text-8xl leading-snug">
						{mainLabel}
						{mainLabel === "금액" ? "을" : "를"} 입력하시고
						<br />
						<span className="text-green">확인</span>을 눌러주십시오
						{(mainLabel === "비밀번호" || mainLabel === "주민등록번호") && (
							<>
								<br />
								<span className="text-6xl text-red">* 입력하실 때 주의하세요!</span>
							</>
						)}
					</span>
				</div>
				<div className="flex flex-col p-10 gap-16 bg-background rounded-2xl">
					<div className="flex justify-between">
						<span className="text-8xl text-blue">{subLabel}</span>
						<span className="text-8xl text-blue">1:00</span>
					</div>
					<div className="flex flex-col">
						<input
							className="flex-1 text-8xl rounded-2xl border-2 border-black text-right px-2.5"
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
