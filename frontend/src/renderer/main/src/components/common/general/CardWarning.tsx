import { Link } from "react-router-dom";
import warningCard from "@renderer/assets/images/warning_card.png";

interface CardWarningProps {
	link: string;
}

export const CardWarning = ({ link }: CardWarningProps): JSX.Element => {
	return (
		<div className="flex flex-col gap-10">
			<div className="flex flex-col gap-5 bg-white rounded-3xl p-10 shadow-custom text-center">
				<span className="text-7xl font-bold break-all leading-tight">
					<span className="text-red">불법 카드</span> 복제 관련
				</span>
				<span className="text-5xl font-bold break-all leading-snug">
					최근 <span className="text-red">자동화 기기</span>에{" "}
					<span className="text-red">카드 복제기</span>를 부착하여 고객 정보 탈취를 시도한 사례가
					있사오니, <span className="text-red">카드 투입부</span>가 아래 화면과 다른 경우에는 사용을
					중단하시고 <span className="text-red">영업점</span>이나{" "}
					<span className="text-red">인터폰</span>으로 신고하여 주시기 바랍니다.{" "}
				</span>
				<div className="flex justify-center items-center">
					<img className="w-56" src={warningCard} alt="warning_card" />
				</div>
			</div>
			<div className="grid grid-cols-2 gap-5 text-white text-7xl font-bold">
				<button type="button" className="p-10 bg-green rounded-3xl">
					<Link to={link}>계속 거래</Link>
				</button>
				<button type="button" className="p-10 bg-red rounded-3xl">
					<Link to="/general/final">거래 취소</Link>
				</button>
			</div>
		</div>
	);
};

export default CardWarning;
