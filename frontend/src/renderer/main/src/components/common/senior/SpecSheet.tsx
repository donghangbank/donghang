import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import { Link } from "react-router-dom";
import { formatAmount } from "@renderer/utils/formatters";
import { motion } from "framer-motion";
import billCheck from "@renderer/assets/audios/bill_check.mp3?url";

interface Section {
	label: string;
	value: string | number | null;
	formatValue?: boolean;
}

interface SpecSheetProps {
	sections: Section[];
	prev?: string;
	link?: string;
	title?: string;
	audioFile?: string;
	dialogue?: string;
	className?: string;
	buttonText?: string;
}

export default function SpecSheet({
	sections,
	prev = "/",
	link = "/senior/final",
	audioFile = billCheck,
	dialogue = "명세표를 확인해주세요",
	className = "",
	buttonText = "확인"
}: SpecSheetProps): JSX.Element {
	useActionPlay({
		audioFile,
		dialogue,
		shouldActivate: true,
		avatarState: "idle"
	});

	const isReady = sections.every((section) => !!section.value);

	return (
		<div className={`flex w-full h-full justify-end items-center ${className}`}>
			<TestButton prevRoute={prev} nextRoute={link} />

			<motion.div
				initial={{ opacity: 0, y: 20 }}
				animate={isReady ? { opacity: 1, y: 0 } : { opacity: 0 }}
				transition={{ duration: 0.5 }}
				className="bg-white p-5 flex flex-col gap-6 mr-24 rounded-3xl"
			>
				{sections.map((section, index) => (
					<div key={index} className="flex flex-col gap-6 justify-center items-start">
						<span className="text-blue text-3xl font-bold">{section.label}</span>
						<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[400px]">
							<span>
								{section.formatValue ? formatAmount(String(section.value)) : section.value}
							</span>
						</div>
					</div>
				))}

				<div className="flex justify-center items-center">
					<button type="button" className="p-6 bg-blue rounded-xl w-full">
						<Link to={link}>
							<span className="text-3xl text-white">{buttonText}</span>
						</Link>
					</button>
				</div>
			</motion.div>
		</div>
	);
}
