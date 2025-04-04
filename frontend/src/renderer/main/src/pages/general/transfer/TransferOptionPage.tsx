import { Link } from "react-router-dom";
import card from "@renderer/assets/images/card.png";
import bankbook from "@renderer/assets/images/bankbook.png";
import numberpad from "@renderer/assets/images/numberpad.png";

export const TransferOptionPage = (): JSX.Element => {
	return (
		<div className="flex flex-col gap-10">
			<div className="bg-white flex flex-col  rounded-3xl py-10 px-32 gap-10 shadow-custom text-center">
				<span className="text-6xl font-bold text-center">
					<span className="text-blue">무엇</span>을 가져오셨나요?
				</span>
				<div className="flex flex-col gap-5 text-5xl font-bold">
					<Link to={"/general/transfer/card/input"}>
						<div className="bg-purple-linear flex justify-between items-center w-full py-5 px-16 rounded-3xl ">
							<span className=" text-white">카드</span>
							<img className="w-40" src={card} alt="card" />
						</div>
					</Link>
					<div className="bg-purple-linear flex justify-between items-center w-full py-5 px-16 rounded-3xl ">
						<span className=" text-white">통장</span>
						<img className="w-40" src={bankbook} alt="bankbook" />
					</div>
					<div className="bg-purple-linear flex justify-between items-center w-full py-5 px-16 rounded-3xl ">
						<span className=" text-white">계좌번호</span>
						<img className="w-28" src={numberpad} alt="numberpad" />
					</div>
				</div>
			</div>
			<div className="flex flex-col text-white text-7xl font-bold">
				<button type="button" className="p-10 bg-red rounded-3xl">
					<Link to="/general/final">거래 취소</Link>
				</button>
			</div>
		</div>
	);
};

export default TransferOptionPage;
