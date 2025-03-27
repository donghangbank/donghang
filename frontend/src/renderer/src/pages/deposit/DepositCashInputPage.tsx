import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const DepositCashInputPage = (): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate("/deposit/cash/counting");
		}, 1000);
	}, [navigate]);

	return (
		<div className="flex h-screen items-center justify-center">
			<span className=" text-8xl font-bold text-center leading-snug">
				<span className="text-red">현금</span>을 넣어주십시오
			</span>
		</div>
	);
};

export default DepositCashInputPage;
