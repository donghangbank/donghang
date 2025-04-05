import { Link } from "react-router-dom";

export const GeneralMain = (): JSX.Element => {
	return (
		<div className="flex flex-col bg-white rounded-3xl p-10 shadow-custom gap-10">
			<span className="text-6xl font-bold text-center">
				<span className="text-blue">어떤 업무</span>를 도와드릴까요?
			</span>
			<div className="grid grid-cols-2 grid-rows-2 gap-10 font-bold text-6xl text-center">
				<Link to="/general/deposit/warning/scam">
					<div className="h-[200px] leading-[200px] bg-cloudyBlue shadow-custom rounded-3xl">
						입금
					</div>
				</Link>
				<Link to="/general/withdrawal/warning/scam">
					<div className="h-[200px] leading-[200px] bg-cloudyBlue shadow-custom rounded-3xl">
						출금
					</div>
				</Link>
				<Link to="/general/transfer/warning/scam">
					<div className="h-[200px] leading-[200px] bg-cloudyBlue shadow-custom rounded-3xl">
						이체
					</div>
				</Link>
				<Link to="/general/inquiry/option">
					<div className="h-[200px] leading-[200px] bg-cloudyBlue shadow-custom rounded-3xl">
						조회
					</div>
				</Link>
			</div>
		</div>
	);
};

export default GeneralMain;
