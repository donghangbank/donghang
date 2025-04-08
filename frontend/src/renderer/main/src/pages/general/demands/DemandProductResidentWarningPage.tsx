import { Link } from "react-router-dom";

export const DemandProductResidentWarningPage = (): JSX.Element => {
	return (
		<div className="flex flex-col gap-10">
			<div className="flex flex-col gap-5 bg-white rounded-3xl p-10 shadow-custom text-center">
				<span className="text-7xl font-bold break-all leading-tight">
					<span className="text-red">신분증 도용</span> 관련
				</span>
				<span className="text-5xl font-bold break-all leading-snug">
					최근 <span className="text-red">신분증 도용</span>을 활용하여 대포 통장을 개설하는 사례가
					있사오니, <span className="text-red">다른 신분증</span>일 경우에는 사용을 중단하기
					바랍니다
				</span>
			</div>
			<div className="grid grid-cols-2 gap-5 text-white text-7xl font-bold">
				<button type="button" className="p-10 bg-green rounded-3xl">
					<Link to="/general/demandproducts/resident/input">계속 거래</Link>
				</button>
				<button type="button" className="p-10 bg-red rounded-3xl">
					<Link to="/general/final">거래 취소</Link>
				</button>
			</div>
		</div>
	);
};

export default DemandProductResidentWarningPage;
