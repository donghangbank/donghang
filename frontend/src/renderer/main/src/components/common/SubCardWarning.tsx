import { IoMdClose } from "react-icons/io";
import { FaRegCircle } from "react-icons/fa6";
import { motion } from "framer-motion";

export default function SubCardWarning(): JSX.Element {
	const handleConfirmClick = (): void => {
		// 서브 로직이 있다면 수행
		// 그 후 메인 쪽에 "confirm" 알림
		window.subAPI.notifyButtonAction("confirm");
	};

	const handleCancelClick = (): void => {
		// 서브 로직(필요하다면 수행)
		// 그 후 메인 쪽에 "cancel" 알림
		window.subAPI.notifyButtonAction("cancel");
	};
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
						<span className="text-red">불법 카드</span> 복제 관련
						<br />
						최근 <span className="text-red">자동화 기기</span>에{" "}
						<span className="text-red">카드 복제기</span>를 부착하여
						<br />
						고객 정보 탈취를 시도한 사례가 있으오니,
					</p>
					<p>
						<span className="text-red">카드 투입부</span>가 아래 화면과 다른 경우에는
						<br />
						사용을 중단하시고 <span className="text-red">영업점</span>이나{" "}
						<span className="text-red">인터폰</span>으로
						<br />
						신고하여 주시기 바랍니다.
					</p>
				</div>
			</div>
			<div className="h-[20%] grid grid-cols-2 gap-2.5 text-6xl font-bold text-white">
				<button onClick={handleConfirmClick} className="block">
					<div className="w-full h-full bg-green inline-flex items-center justify-center gap-8">
						<FaRegCircle className="size-18" />
						<span>확인</span>
					</div>
				</button>
				<button onClick={handleCancelClick} className="block">
					<div className="w-full h-full bg-red inline-flex items-center justify-center">
						<IoMdClose className="size-24" />
						<span>취소</span>
					</div>
				</button>
			</div>
		</motion.div>
	);
}
