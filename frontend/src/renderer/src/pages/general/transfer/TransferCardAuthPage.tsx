import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const TransferCardAuthPage = (): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate("/general/transfer/card/password");
		}, 2000);
	}, [navigate]);

	return (
		<div className="flex flex-col bg-white rounded-3xl px-32 py-10 shadow-custom gap-10 justify-center items-center">
			<span className="text-6xl font-bold text-center leading-snug">
				<span className="text-red">카드</span>를 읽고 있습니다 <br />
				잠시만 기다려 주세요
			</span>
		</div>
	);
};
