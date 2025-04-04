import { useState, useEffect, useRef } from "react";
import { useLocation } from "react-router-dom";
import cash from "@renderer/assets/images/cash.png";

export const CashSlot = (): JSX.Element => {
	const location = useLocation();
	const isInfiniteMode =
		location.pathname.includes("/cash/input") || location.pathname.includes("/cash/output");

	const [isOpen, setIsOpen] = useState(false);
	const [isInserted, setIsInserted] = useState(false);
	const intervalRef = useRef<NodeJS.Timeout | null>(null);
	const timeoutsRef = useRef<NodeJS.Timeout[]>([]);

	useEffect(() => {
		if (isInfiniteMode) {
			const runCycle = (): void => {
				// 투입구 열림
				setIsOpen(true);
				// 300ms 후 돈 넣는 애니메이션 실행
				const timeout1 = setTimeout(() => {
					setIsInserted(true);
				}, 300);
				// 1000ms 후 돈 넣기 애니메이션 종료 후 투입구 닫힘
				const timeout2 = setTimeout(() => {
					setIsInserted(false);
					setIsOpen(false);
				}, 1300);
				timeoutsRef.current.push(timeout1, timeout2);
			};

			// 즉시 사이클 실행 후 3000ms 주기로 반복
			runCycle();
			intervalRef.current = setInterval(runCycle, 3000);

			return (): void => {
				if (intervalRef.current) {
					clearInterval(intervalRef.current);
					intervalRef.current = null;
				}
				for (const t of timeoutsRef.current) {
					clearTimeout(t);
				}
				timeoutsRef.current = [];
			};
		}

		setIsOpen(false);
		setIsInserted(false);
	}, [isInfiniteMode]);

	return (
		<div className="relative h-full w-full cursor-pointer bg-gray-300 px-52 py-5">
			<div className="w-full h-full bg-gray-700 rounded-2xl">
				<div
					className="w-full h-1/2 border-b bg-gray-500 border-black transition-transform duration-300 rounded-tl-xl rounded-tr-xl"
					style={{
						transform: isOpen ? "rotateX(55deg)" : "rotateX(0deg)",
						transformOrigin: "top"
					}}
				/>
				<div
					className="w-full h-1/2 border-t bg-gray-500 border-black transition-transform duration-300 rounded-bl-xl rounded-br-xl"
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
