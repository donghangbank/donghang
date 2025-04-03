import { useContextReset } from "@renderer/hooks/useContextReset";
import { useInputReset } from "@renderer/hooks/useInputReset";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const FinalPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { resetInputContext } = useInputReset();
	const { resetContext } = useContextReset();

	useEffect(() => {
		resetInputContext();
		resetContext();
	}, [resetInputContext, resetContext]);

	useEffect(() => {
		setTimeout(() => {
			navigate("/");
		}, 2000);
	}, [navigate]);

	return (
		<div className="flex flex-col h-full  p-10">
			<div className="flex-1 flex flex-col items-center justify-center w-full bg-white p-10 rounded-2xl shadow-custom">
				<span className="text-6xl font-bold text-center leading-snug">
					감사합니다
					<br />
					놓고 가신 물건이 없는지
					<br />
					확인해주시기 바랍니다
				</span>
			</div>
		</div>
	);
};

export default FinalPage;
