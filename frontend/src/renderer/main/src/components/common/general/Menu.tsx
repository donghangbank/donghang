import { Link } from "react-router-dom";
import { motion } from "framer-motion";

interface MenuProps {
	prompts: { prompt: string; link: string }[];
}

export const Menu = ({ prompts }: MenuProps): JSX.Element => {
	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col bg-white rounded-3xl p-10 shadow-custom gap-10"
		>
			<span className="text-6xl font-bold text-center">
				<span className="text-blue">어떤 업무</span>를 도와드릴까요?
			</span>
			<div
				className={`grid grid-cols-2 ${prompts.length > 2 && "grid-rows-2"} gap-10 font-bold text-6xl text-center`}
			>
				{prompts.map((prompt, index) => (
					<Link to={prompt.link} key={`${index}.${prompt.prompt}`}>
						<div className="h-[200px] leading-[200px] bg-cloudyBlue shadow-custom rounded-3xl">
							{prompt.prompt}
						</div>
					</Link>
				))}
			</div>
		</motion.div>
	);
};

export default Menu;
