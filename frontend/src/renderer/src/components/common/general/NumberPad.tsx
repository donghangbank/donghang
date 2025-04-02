import { Link } from "react-router-dom";
import NumberButton from "../NumberButton";
import { InputContext } from "@renderer/contexts/InputContext";
import { useContext } from "react";

interface NumberPadProps {
	setInputValue: React.Dispatch<React.SetStateAction<string>>;
	type?: string;
	onConfirm: () => void;
}

export const NumberPad = ({ setInputValue, type, onConfirm }: NumberPadProps): JSX.Element => {
	const { disabled } = useContext(InputContext);
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
		if (disabled) return;
		setInputValue((prev) => {
			if (type === "amount" && prev === "0") return num;
			if (prev.length < getMaxLength()) return prev + num;
			return prev;
		});
	};

	const handleNumberDeleteClick = (): void => {
		if (disabled) return;
		setInputValue((prev) => prev.slice(0, -1));
	};

	const handleNumberClearClick = (): void => {
		if (disabled) return;
		setInputValue("");
	};

	return (
		<>
			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				{["1", "2", "3"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor={disabled ? "bg-gray-300" : "bg-cloudyBlue"}
						isSquare
						textColor={disabled ? "gray-400" : "gray-600"}
						onClick={!disabled ? (): void => handleNumberClick(num) : undefined}
					/>
				))}
			</div>
			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				{["4", "5", "6"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor={disabled ? "bg-gray-300" : "bg-cloudyBlue"}
						isSquare
						textColor={disabled ? "gray-400" : "gray-600"}
						onClick={!disabled ? (): void => handleNumberClick(num) : undefined}
					/>
				))}
			</div>
			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				{["7", "8", "9"].map((num) => (
					<NumberButton
						key={`${num}-button`}
						text={num}
						bgColor={disabled ? "bg-gray-300" : "bg-cloudyBlue"}
						isSquare
						textColor={disabled ? "gray-400" : "gray-600"}
						onClick={!disabled ? (): void => handleNumberClick(num) : undefined}
					/>
				))}
			</div>

			<div className="h-[20%] grid grid-cols-3 gap-2.5">
				<NumberButton text={"지움"} bgColor="bg-blue" isSquare onClick={handleNumberDeleteClick} />
				<NumberButton
					text={"0"}
					bgColor={disabled ? "bg-gray-300" : "bg-cloudyBlue"}
					isSquare
					textColor={disabled ? "gray-400" : "gray-600"}
					onClick={!disabled ? (): void => handleNumberClick("0") : undefined}
				/>
				<NumberButton text={"정정"} bgColor="bg-blue" isSquare onClick={handleNumberClearClick} />
			</div>

			<div className="h-[20%] grid grid-cols-2 gap-2.5">
				<NumberButton text={"확인"} bgColor="bg-green" isSquare={false} onClick={onConfirm} />
				<Link to={"/general/final"}>
					<NumberButton text={"취소"} bgColor="bg-red" isSquare={false} />
				</Link>
			</div>
		</>
	);
};

export default NumberPad;
