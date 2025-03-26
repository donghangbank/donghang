import { IoMdClose } from "react-icons/io";
import { FaRegCircle } from "react-icons/fa6";
import { Link } from "react-router-dom";

interface ConfirmPanelProps {
	title: string;
	items: { text: string; value: string }[];
}

export const ConfirmPanel = ({ title, items }: ConfirmPanelProps): JSX.Element => {
	return (
		<div className="flex flex-col h-screen justify-between">
			<div className="m-10 p-10 flex flex-col rounded-2xl bg-white flex-1 justify-between">
				<span className="my-20 text-8xl font-bold text-center">{title}</span>
				<div className="flex flex-col p-5 gap-5 bg-background rounded-2xl text-7xl font-bold leading-snug text-center">
					{items.map((item, index) => (
						<div className="flex" key={index}>
							<span className="flex-1">{item.text}</span>
							<span className="flex-1 bg-white rounded-2xl">{item.value}</span>
						</div>
					))}
				</div>
			</div>
			<div className="h-[20%] grid grid-cols-2 gap-2.5 text-8xl font-bold text-white">
				<Link to="/" className="block">
					<div className="w-full h-full bg-green inline-flex items-center justify-center gap-8">
						<FaRegCircle className="size-28" />
						<span>확인</span>
					</div>
				</Link>
				<Link to="/" className="block">
					<div className="w-full h-full bg-red inline-flex items-center justify-center">
						<IoMdClose className="size-40" />
						<span>취소</span>
					</div>
				</Link>
			</div>
		</div>
	);
};

export default ConfirmPanel;
