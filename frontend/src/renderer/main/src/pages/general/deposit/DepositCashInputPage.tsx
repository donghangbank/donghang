import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const DepositCashInputPage = (): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate("/general/deposit/cash/counting");
		}, 3000);
	}, [navigate]);

	return (
		<div className="flex flex-col h-full  p-10">
			<div className="flex-1 flex flex-col items-center justify-center w-full bg-white p-10 rounded-2xl shadow-custom">
				<span className=" text-8xl font-bold text-center leading-snug">
					<span className="text-red">현금</span>을 넣어주십시오
				</span>
			</div>
		</div>
	);
};

export default DepositCashInputPage;
