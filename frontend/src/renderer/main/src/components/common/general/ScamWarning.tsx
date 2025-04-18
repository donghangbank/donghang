import { Link } from "react-router-dom";
import { motion } from "framer-motion";

interface ScamWarningProps {
	link: string;
}

export const ScamWarning = ({ link }: ScamWarningProps): JSX.Element => {
	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col gap-10"
		>
			<div className="bg-white rounded-3xl p-10 shadow-custom text-center">
				<span className="text-6xl font-bold break-all leading-tight">
					최근 <span className="text-red">공공기관</span>
					{"(국세청, 경찰청, 검찰청, 금감원 등)이나"}{" "}
					<span className="text-red">금융기관 직원</span>을{" "}
					<span className="text-red">사칭한 금융사기</span>가<br />
					자주 발생하고 있습니다.
					<br />
					현금을 요구받으면
					<br />
					<span className="text-red">100% 보이스피싱</span>입니다.
				</span>
			</div>
			<div className="grid grid-cols-2 gap-5 text-white text-7xl font-bold">
				<button type="button" className="p-10 bg-green rounded-3xl">
					<Link to={link}>계속 거래</Link>
				</button>
				<button type="button" className="p-10 bg-red rounded-3xl">
					<Link to={"/general/final"}>거래 취소</Link>
				</button>
			</div>
		</motion.div>
	);
};

export default ScamWarning;
