import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import { Link } from "react-router-dom";
import {
	formatAmount,
	formatAccountNumber,
	formatResidentNumber,
	formatPassword,
	formatTransactionTime,
	formatDefault
} from "@renderer/utils/formatters";
import { motion } from "framer-motion";
import billCheck from "@renderer/assets/audios/bill_check.mp3?url";
import { useEffect, useState } from "react";

interface Section {
	label: string;
	value: string | number | null;
	formatType?: "amount" | "account" | "resident" | "password" | "datetime" | "default";
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

const formatters: Record<NonNullable<Section["formatType"]>, (value: string) => string> = {
	amount: formatAmount,
	account: formatAccountNumber,
	resident: formatResidentNumber,
	password: formatPassword,
	datetime: formatTransactionTime,
	default: formatDefault
};

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

	const [isVisible, setIsVisible] = useState(false);

	useEffect(() => {
		const timer = setTimeout(() => setIsVisible(true), 1000);
		return (): void => clearTimeout(timer);
	}, []);

	return (
		<div className={`flex w-full h-full justify-end items-center ${className}`}>
			<TestButton prevRoute={prev} nextRoute={link} />

			<motion.div
				initial={{ opacity: 0, y: 20 }}
				animate={isVisible ? { opacity: 1, y: 0 } : { opacity: 0 }}
				transition={{ duration: 0.5 }}
				className="bg-white p-5 flex flex-col gap-6 mr-24 rounded-3xl"
			>
				{sections.map((section, index) => {
					const raw = section.value?.toString() ?? "";
					const formatter = formatters[section.formatType ?? "default"];
					const formattedValue = formatter(raw);

					return (
						<div key={index} className="flex flex-col gap-6 justify-center items-start">
							<span className="text-blue text-3xl font-bold">{section.label}</span>
							<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[400px]">
								<span>{formattedValue}</span>
							</div>
						</div>
					);
				})}

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
