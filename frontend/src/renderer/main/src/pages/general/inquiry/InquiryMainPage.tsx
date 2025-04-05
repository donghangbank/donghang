import { Link } from "react-router-dom";

export const InquiryMainPage = (): JSX.Element => {
	return (
		<div className="flex flex-col bg-white rounded-3xl p-10 shadow-custom gap-10">
			<span className="text-6xl font-bold text-center">
				<span className="text-blue">어떤 업무</span>를 도와드릴까요?
			</span>
			<div className="grid grid-cols-2 gap-10 font-bold text-5xl text-center">
				<Link to="/general/balance/warning/card">
					<div className="h-[200px] leading-[200px] bg-cloudyBlue shadow-custom rounded-3xl">
						잔액 조회
					</div>
				</Link>
				<Link to="/general/history/warning/card">
					<div className="h-[200px] leading-[200px] bg-cloudyBlue shadow-custom rounded-3xl">
						거래 내역 조회
					</div>
				</Link>
			</div>
		</div>
	);
};

export default InquiryMainPage;
