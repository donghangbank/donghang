import { Link } from "react-router-dom";

interface OptionProps {
	title: string;
	prompts: { prompt: string; link: string; imageUrl: string }[];
}

export const Option = ({ title, prompts }: OptionProps): JSX.Element => {
	const match = title.match(/(.+?)([을를])(.*)/);
	const [blueText, particle, restText] = match ? [match[1], match[2], match[3]] : [title, "", ""];

	return (
		<div className="flex flex-col gap-10">
			<div className="bg-white flex flex-col  rounded-3xl py-10 px-20 gap-10 shadow-custom text-center">
				<span className="text-6xl font-bold text-center">
					<span className="text-blue">{blueText}</span>
					{particle}
					{restText}
				</span>
				<div className="flex flex-col gap-5 text-5xl font-bold">
					{prompts.map((prompt, index) => (
						<Link to={prompt.link} key={`${index}.${prompt.prompt}`}>
							<div className="bg-purple-linear flex justify-between items-center w-full py-5 px-16 rounded-3xl ">
								<span className=" text-white">{prompt.prompt}</span>
								<img className="h-28" src={prompt.imageUrl} alt={prompt.prompt} />
							</div>
						</Link>
					))}
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

export default Option;
