import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const WithDrawalCardInputPage = (): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate("/general/withdrawal/card/auth");
		}, 1000);
	}, [navigate]);

	return (
		<div className="flex flex-col h-full  p-10">
			<div className="flex-1 flex flex-col items-center justify-center w-full bg-white p-10 rounded-2xl shadow-custom">
				<span className=" text-8xl font-bold text-center leading-snug">
					<span className="text-red">카드</span>를 넣어주십시오
				</span>
			</div>
		</div>
	);
};

export default WithDrawalCardInputPage;
