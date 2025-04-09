import insert_cash from "@renderer/assets/images/insert_cash.png";
import { useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

interface CashInputProps {
	link: string;
}

export const CashInput = ({ link }: CashInputProps): JSX.Element => {
	const navigate = useNavigate();
	const timerRef = useRef<NodeJS.Timeout | null>(null);

	useEffect(() => {
		timerRef.current = setTimeout(() => {
			navigate(link);
		}, 3000);

		return (): void => {
			if (timerRef.current) {
				clearTimeout(timerRef.current);
			}
		};
	}, [navigate, link]);

	const handleCancel = (): void => {
		if (timerRef.current) {
			clearTimeout(timerRef.current);
			timerRef.current = null;
		}
	};

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col gap-10"
		>
			<div className="flex flex-col bg-white rounded-3xl px-24 py-10 shadow-custom gap-10">
				<span className="text-6xl font-bold text-center">
					<span className="text-red">현금</span>을 넣어주세요
				</span>
				<img src={insert_cash} alt="insert_cash" className="w-[512px]" />
			</div>
			<div className="flex flex-col text-white text-7xl font-bold">
				<button type="button" className="p-10 bg-red rounded-3xl" onClick={handleCancel}>
					<Link to="/general/final">거래 취소</Link>
				</button>
			</div>
		</motion.div>
	);
};

export default CashInput;
