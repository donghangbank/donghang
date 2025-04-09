import { ProductContext } from "@renderer/contexts/ProductContext";
import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

export const DemandProductResidentAuthPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { setMemberId } = useContext(ProductContext);
	const member_id = 1;
	setMemberId(member_id);

	useEffect(() => {
		setTimeout(() => {
			navigate("/general/demandproducts/info/password");
		}, 2000);
	}, [navigate]);

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col bg-white rounded-3xl px-24 py-10 shadow-custom gap-10 justify-center items-center"
		>
			<span className="text-6xl font-bold text-center leading-snug">
				<span className="text-red">신분증</span>을 읽고 있습니다 <br />
				잠시만 기다려 주세요
			</span>
		</motion.div>
	);
};

export default DemandProductResidentAuthPage;
