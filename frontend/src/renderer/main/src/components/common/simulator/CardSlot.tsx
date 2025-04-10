import { useState, useEffect, useRef } from "react";
import { useLocation } from "react-router-dom";
import card from "@renderer/assets/images/card2.png";

export const CardSlot = (): JSX.Element | null => {
	const location = useLocation();
	const [showCard, setShowCard] = useState(false);
	const [isInserted, setIsInserted] = useState(false);
	const [isLightOn, setIsLightOn] = useState(false);
	const intervalRef = useRef<NodeJS.Timeout | null>(null);

	useEffect(() => {
		if (location.pathname.includes("/card/input")) {
			setShowCard(true);
			setIsInserted(true);
			setIsLightOn(true);
			intervalRef.current = setInterval(() => {
				setIsInserted((prev) => !prev);
				setIsLightOn((prev) => !prev);
			}, 500);
		} else {
			setShowCard(false);
		}

		return (): void => {
			if (intervalRef.current) {
				clearInterval(intervalRef.current);
				intervalRef.current = null;
			}
		};
	}, [location.pathname]);

	return (
		<div
			className={`relative h-full w-full flex items-center justify-center ${
				showCard ? (isLightOn ? "bg-gray-100" : "bg-gray-300") : "bg-gray-300"
			} transition-colors`}
		>
			<div className="relative flex flex-col items-center">
				<span className="absolute -top-6 text-sm font-bold text-black z-10">카드 투입구</span>
				<div className="w-36 h-6 p-2 bg-slate-500 rounded-2xl">
					<div className="w-full h-full bg-slate-700 rounded-xl"></div>
				</div>
			</div>
			{showCard && (
				<div className="absolute inset-0 flex items-center justify-center pointer-events-none">
					<img
						src={card}
						alt="카드"
						className="w-48 h-auto object-contain transform transition-transform duration-500"
						style={{
							transform: isInserted
								? "perspective(500px) rotateX(45deg) translateY(-20px)"
								: "perspective(500px) rotateX(45deg) translateY(50px)",
							transformOrigin: "bottom center"
						}}
					/>
				</div>
			)}
		</div>
	);
};

export default CardSlot;
