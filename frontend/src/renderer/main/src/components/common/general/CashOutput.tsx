import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import insert_cash from "@renderer/assets/images/insert_cash.png";
import { motion } from "framer-motion";

interface CashInputProps {
	link: string;
}

export const CashOutput = ({ link }: CashInputProps): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate(link);
		}, 3000);
	}, [navigate, link]);

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col bg-white rounded-3xl px-24 py-10 shadow-custom gap-10"
		>
			<span className="text-6xl font-bold text-center">
				<span className="text-red">현금</span>을 꺼내주세요
			</span>
			<img src={insert_cash} alt="insert_cash" className="w-[512px]" />
		</motion.div>
	);
};

export default CashOutput;
