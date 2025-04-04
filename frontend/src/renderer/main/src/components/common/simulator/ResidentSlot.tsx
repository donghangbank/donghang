import { useState, useEffect, useRef } from "react";
import { useLocation } from "react-router-dom";
import resident from "@renderer/assets/images/resident.png";

export const ResidentSlot = (): JSX.Element | null => {
	const location = useLocation();
	const [showResident, setShowResident] = useState(false);
	const [isInserted, setIsInserted] = useState(false);
	const intervalRef = useRef<NodeJS.Timeout | null>(null);

	useEffect(() => {
		if (location.pathname.includes("/resident/input")) {
			setShowResident(true);
			setIsInserted(true);
			intervalRef.current = setInterval(() => {
				setIsInserted((prev) => !prev);
			}, 500);
		} else {
			setShowResident(false);
		}

		return (): void => {
			if (intervalRef.current) {
				clearInterval(intervalRef.current);
				intervalRef.current = null;
			}
		};
	}, [location.pathname]);

	return (
		<div className="relative h-full w-full bg-gray-300 flex items-center justify-center">
			<div className="relative flex flex-col items-center">
				<span className="absolute -top-6 text-sm font-bold text-black z-10">신분증 투입구</span>
				<div className="w-40 h-6 p-2 bg-gray-500 rounded-2xl">
					<div className="w-full h-full bg-gray-700 rounded-xl"></div>
				</div>
			</div>
			{showResident && (
				<div className="absolute inset-0 flex items-center justify-center pointer-events-none">
					<img
						src={resident}
						alt="신분증"
						className="w-48 h-auto object-contain transform transition-transform duration-500"
						style={{
							transform: isInserted
								? "perspective(500px) rotateX(45deg) translateY(-5px)"
								: "perspective(500px) rotateX(45deg) translateY(50px)",
							transformOrigin: "bottom center"
						}}
					/>
				</div>
			)}
		</div>
	);
};

export default ResidentSlot;
