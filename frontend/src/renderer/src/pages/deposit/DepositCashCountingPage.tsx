import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const DepositCashCountingPage = (): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate("/deposit/confirm");
		}, 1000);
	}, [navigate]);

	return (
		<div className="flex h-screen items-center justify-center">
			<span className=" text-8xl font-bold text-center leading-snug">
				<span className="text-red">현금</span>을 세고 있습니다
				<br /> 잠시만 기다려주십시오
			</span>
		</div>
	);
};

export default DepositCashCountingPage;
