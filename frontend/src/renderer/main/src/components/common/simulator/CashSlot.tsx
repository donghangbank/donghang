import { useState } from "react";
import cash from "@renderer/assets/images/cash.png";

export const CardSlot = (): JSX.Element => {
	const [isOpen, setIsOpen] = useState(false);
	const [isInserted, setIsInserted] = useState(false);

	const handleMouseEnter = (): void => setIsOpen(true);

	const handleMouseLeave = (): void => {
		setIsOpen(false);
		setIsInserted(false);
	};

	const handleClick = (): void => {
		setIsInserted(true);
		setTimeout(() => {
			setIsInserted(false);
		}, 1000);
	};

	return (
		<div
			className="relative h-full w-full cursor-pointer bg-gray-300 px-52 py-5"
			onMouseEnter={handleMouseEnter}
			onMouseLeave={handleMouseLeave}
			onClick={handleClick}
			role="button"
			aria-label="현금 투입구"
		>
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
		</div>
	);
};

export default CardSlot;
