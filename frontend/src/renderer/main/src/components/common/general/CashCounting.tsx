import { InputContext } from "@renderer/contexts/InputContext";
import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

interface CashCountingProps {
	link: string;
}

export const CashCounting = ({ link }: CashCountingProps): JSX.Element => {
	const navigate = useNavigate();

	const { setAmount } = useContext(InputContext);

	useEffect(() => {
		setTimeout(() => {
			setAmount("50000");
			navigate(link);
		}, 3000);
	}, [navigate, link, setAmount]);

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col bg-white rounded-3xl px-24 py-10 shadow-custom gap-10"
		>
			<span className="text-6xl font-bold text-center leading-snug">
				<span className="text-red">현금</span>을 세고 있어요
				<br /> 잠시만 기다려주세요
			</span>
		</motion.div>
	);
};

export default CashCounting;
