import { useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import insert_card from "@renderer/assets/images/insert_card.png";

interface CardInputProps {
	link: string;
}

export const CardInput = ({ link }: CardInputProps): JSX.Element => {
	const navigate = useNavigate();
	const timerRef = useRef<NodeJS.Timeout | null>(null);

	useEffect(() => {
		timerRef.current = setTimeout(() => {
			navigate(link);
		}, 3000);

		return (): void => {
			if (timerRef.current) {
				clearTimeout(timerRef.current);
			}
		};
	}, [navigate, link]);

	const handleCancel = (): void => {
		if (timerRef.current) {
			clearTimeout(timerRef.current);
			timerRef.current = null;
		}
	};

	return (
		<div className="flex flex-col gap-10">
			<div className="flex flex-col bg-white rounded-3xl px-24 py-10 shadow-custom gap-10">
				<span className="text-6xl font-bold text-center">
					<span className="text-red">카드</span>를 넣어주세요
				</span>
				<img src={insert_card} alt="insert_card" className="w-[512px]" />
			</div>
			<div className="flex flex-col text-white text-7xl font-bold">
				<button type="button" className="p-10 bg-red rounded-3xl" onClick={handleCancel}>
					<Link to="/general/final">거래 취소</Link>
				</button>
			</div>
		</div>
	);
};

export default CardInput;
