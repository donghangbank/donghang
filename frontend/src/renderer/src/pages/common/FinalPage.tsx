import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const FinalPage = (): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate("/");
		}, 2000);
	}, [navigate]);

	return (
		<div className="flex h-screen items-center justify-center">
			<span className=" text-8xl font-bold text-center leading-snug">
				감사합니다
				<br />
				놓고 가신 물건이 없는지
				<br />
				확인해주시기 바랍니다
			</span>
		</div>
	);
};

export default FinalPage;
