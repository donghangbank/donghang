import insert_card from "@renderer/assets/images/insert_card.png";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const TransferCardInputPage = (): JSX.Element => {
	const navigate = useNavigate();

	useEffect(() => {
		setTimeout(() => {
			navigate("/general/transfer/card/auth");
		}, 3000);
	}, [navigate]);

	return (
		<div className="flex flex-col bg-white rounded-3xl px-32 py-10 shadow-custom gap-10">
			<span className="text-6xl font-bold text-center">
				<span className="text-red">카드</span>를 넣어주십시오
			</span>
			<img src={insert_card} alt="insert_card" className="w-[512px]" />
		</div>
	);
};

export default TransferCardInputPage;
