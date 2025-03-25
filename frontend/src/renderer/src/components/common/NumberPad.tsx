import { Link } from "react-router-dom";
import NumberButton from "./NumberButton";
import PropTypes from "prop-types";

interface NumberPadProps {
	setInputValue: React.Dispatch<React.SetStateAction<string>>;
	type?: string;
}

export const NumberPad: React.FC<NumberPadProps> = ({ setInputValue, type }) => {
	const getMaxLength = (): number => {
		switch (type) {
			case "account":
				return 12;
			case "resident":
				return 13;
			case "password":
				return 4;
			default:
				return 11;
		}
	};

	const handleNumberClick = (num: string): void => {
		setInputValue((prev) => {
			if (prev.length < getMaxLength()) {
				return prev + num;
			}
			return prev;
		});
	};

	const handleNumberDeleteClick = (): void => {
		setInputValue((prev) => prev.slice(0, -1));
	};

	const handleNumberClearClick = (): void => {
		setInputValue("");
	};

	return (
		<div className="h-screen flex flex-col gap-2.5">
			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				{["1", "2", "3"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor="bg-green"
						isSquare
						onClick={() => handleNumberClick(num)}
					/>
				))}
			</div>
			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				{["4", "5", "6"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor="bg-green"
						isSquare
						onClick={() => handleNumberClick(num)}
					/>
				))}
			</div>
			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				{["7", "8", "9"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor="bg-green"
						isSquare
						onClick={() => handleNumberClick(num)}
					/>
				))}
			</div>

			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				<NumberButton text={"지움"} bgColor="bg-blue" isSquare onClick={handleNumberDeleteClick} />
				<NumberButton
					text={"0"}
					bgColor="bg-green"
					isSquare
					onClick={() => handleNumberClick("0")}
				/>
				<NumberButton text={"정정"} bgColor="bg-blue" isSquare onClick={handleNumberClearClick} />
			</div>

			<div className="h-[20vh] grid grid-cols-2 gap-2.5">
				<NumberButton text={"확인"} bgColor="bg-green" isSquare={false} />
				<Link to={"/"}>
					<NumberButton text={"취소"} bgColor="bg-red" isSquare={false} />
				</Link>
			</div>
		</div>
	);
};

NumberPad.propTypes = {
	setInputValue: PropTypes.func.isRequired,
	type: PropTypes.string
};

export default NumberPad;
