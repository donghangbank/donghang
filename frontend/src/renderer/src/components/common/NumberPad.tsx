import { Link } from "react-router-dom";
import NumberButton from "./NumberButton";

export const NumberPad: React.FC = () => {
	return (
		<div className="h-screen flex flex-col gap-2.5">
			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				{["1", "2", "3"].map((num) => (
					<NumberButton key={`${num}-button`} text={num} bgColor="bg-green" isSquare />
				))}
			</div>
			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				{["4", "5", "6"].map((num) => (
					<NumberButton key={`${num}-button`} text={num} bgColor="bg-green" isSquare />
				))}
			</div>
			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				{["7", "8", "9"].map((num) => (
					<NumberButton key={`${num}-button`} text={num} bgColor="bg-green" isSquare />
				))}
			</div>

			<div className="h-[20vh] grid grid-cols-3 gap-2.5">
				<NumberButton text={"지움"} bgColor="bg-blue" isSquare />
				<NumberButton text={"0"} bgColor="bg-green" isSquare />
				<NumberButton text={"정정"} bgColor="bg-blue" isSquare />
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

export default NumberPad;
