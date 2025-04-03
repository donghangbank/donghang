import { IoMdClose } from "react-icons/io";
import { FaRegCircle } from "react-icons/fa6";
import { Link, useLocation } from "react-router-dom";
import { motion } from "framer-motion";

interface ScamWarningProps {
	link: string;
}

export default function ScamWarning({ link }: ScamWarningProps): JSX.Element {
	const location = useLocation();
	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col justify-between"
		>
			<div className="my-2.5 p-10 flex flex-col rounded-2xl bg-white flex-1 justify-between">
				<div className="flex flex-col p-5 gap-5 bg-background rounded-2xl text-5xl font-bold leading-snug text-center">
					<p>
						최근 <span className="text-red">공공기관</span>
						(국세청, 경찰청, 검찰청, 금감원 등)이나
						<br />
						<span className="text-red">금융기관 직원</span>을 사칭한{" "}
						<span className="text-red">금융사기</span>가<br />
						자주 발생하고 있습니다.
					</p>
					<p>
						현금을 요구받으면 <span className="text-red">100% 보이스 피싱</span>입니다.
					</p>
				</div>
			</div>
			<div className="h-[20%] grid grid-cols-2 gap-2.5 text-6xl font-bold text-white">
				<Link to={link} className="block">
					<div className="w-full h-full bg-green inline-flex items-center justify-center gap-8">
						<FaRegCircle className="size-18" />
						<span>확인</span>
					</div>
				</Link>
				<Link to={location.pathname.includes("specsheet") ? link : "/"} className="block">
					<div className="w-full h-full bg-red inline-flex items-center justify-center">
						<IoMdClose className="size-24" />
						<span>취소</span>
					</div>
				</Link>
			</div>
		</motion.div>
	);
}
