import { useState, useEffect, useRef } from "react";
import { useLocation } from "react-router-dom";
import cash from "@renderer/assets/images/cash.png";

export const CashSlot = (): JSX.Element => {
	const location = useLocation();
	const isInfiniteMode =
		location.pathname.includes("/cash/input") || location.pathname.includes("/cash/output");

	const [isOpen, setIsOpen] = useState(false);
	const [isInserted, setIsInserted] = useState(false);
	const [isLightOn, setIsLightOn] = useState(false);
	const intervalRef = useRef<NodeJS.Timeout | null>(null);
	const blinkIntervalRef = useRef<NodeJS.Timeout | null>(null);
	const timeoutsRef = useRef<NodeJS.Timeout[]>([]);

	useEffect(() => {
		if (isInfiniteMode) {
			const runCycle = (): void => {
				setIsOpen(true);
				const timeout1 = setTimeout(() => {
					setIsInserted(true);
				}, 300);
				const timeout2 = setTimeout(() => {
					setIsInserted(false);
					setIsOpen(false);
				}, 1300);
				timeoutsRef.current.push(timeout1, timeout2);
			};

			// Start cash animation cycle
			runCycle();
			intervalRef.current = setInterval(runCycle, 3000);

			// Start blinking interval
			blinkIntervalRef.current = setInterval(() => {
				setIsLightOn((prev) => !prev);
			}, 500);

			return (): void => {
				if (intervalRef.current) clearInterval(intervalRef.current);
				if (blinkIntervalRef.current) clearInterval(blinkIntervalRef.current);
				intervalRef.current = null;
				blinkIntervalRef.current = null;
				timeoutsRef.current.forEach(clearTimeout);
				timeoutsRef.current = [];
			};
		}

		// Reset states when not in infinite mode
		setIsOpen(false);
		setIsInserted(false);
		setIsLightOn(false);
	}, [isInfiniteMode]);

	return (
		<div
			className={`relative h-full w-full cursor-pointer px-52 py-5 ${
				isInfiniteMode ? (isLightOn ? "bg-gray-100" : "bg-gray-300") : "bg-gray-300"
			} transition-colors`}
		>
			<div className="w-full h-full bg-gray-700 rounded-2xl">
				<div
					className="w-full h-1/2 border-b bg-slate-500 border-black transition-transform duration-300 rounded-tl-xl rounded-tr-xl"
					style={{
						transform: isOpen ? "rotateX(55deg)" : "rotateX(0deg)",
						transformOrigin: "top"
					}}
				/>
				<div
					className="w-full h-1/2 border-t bg-slate-500 border-black transition-transform duration-300 rounded-bl-xl rounded-br-xl"
					style={{
						transform: isOpen ? "rotateX(-55deg)" : "rotateX(0deg)",
						transformOrigin: "bottom"
					}}
				/>
			</div>
			<div className="absolute inset-0 flex items-center justify-center">
				<span className="text-sm font-bold text-white">현금 투입구</span>
			</div>
			{isInfiniteMode && (
				<div className="absolute inset-0 flex items-center justify-center pointer-events-none">
					<img
						src={cash}
						alt="현금"
						className="w-72 h-auto object-contain transform transition-transform duration-500"
						style={{
							transform: isInserted
								? "perspective(500px) rotateX(-45deg) translateY(-70px)"
								: "perspective(500px) rotateX(-45deg) translateY(-110px)",
							transformOrigin: "bottom center"
						}}
					/>
				</div>
			)}
		</div>
	);
};

export default CashSlot;
