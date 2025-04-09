import {
	formatAmount,
	formatAccountNumber,
	formatResidentNumber,
	formatPassword,
	formatTransactionTime,
	formatDefault
} from "@renderer/utils/formatters";
import { motion } from "framer-motion";
import { useEffect, useState } from "react";

interface Section {
	label: string;
	value: string | number | null;
	formatType?: "amount" | "account" | "resident" | "password" | "datetime" | "default";
}

interface ProductSheetProps {
	sections: Section[];
	prev?: string;
	link?: string;
	title?: string;
	audioFile?: string;
	dialogue?: string;
	className?: string;
	buttonText?: string;
	width?: number;
	buttonText1?: string;
	buttonText2?: string;
}

const formatters: Record<NonNullable<Section["formatType"]>, (value: string) => string> = {
	amount: formatAmount,
	account: formatAccountNumber,
	resident: formatResidentNumber,
	password: formatPassword,
	datetime: formatTransactionTime,
	default: formatDefault
};

export default function ProductSheet({
	sections,
	className = "",
	width = 400
}: ProductSheetProps): JSX.Element {
	const [isVisible, setIsVisible] = useState(false);

	useEffect(() => {
		const timer = setTimeout(() => setIsVisible(true), 1000);
		return (): void => clearTimeout(timer);
	}, []);

	return (
		<div className={`flex w-full h-full justify-end items-center ${className}`}>
			<motion.div
				initial={{ opacity: 0, y: 20 }}
				animate={isVisible ? { opacity: 1, y: 0 } : { opacity: 0 }}
				transition={{ duration: 0.5 }}
				className="bg-white p-5 flex flex-col gap-4 mr-24 rounded-3xl"
			>
				{sections.map((section, index) => {
					const raw = section.value?.toString() ?? "";
					const formatter = formatters[section.formatType ?? "default"];
					const formattedValue = formatter(raw);

					return (
						<div key={index} className="flex flex-col gap-4 justify-center items-start">
							<span className="text-blue text-3xl font-bold">{section.label}</span>
							<div
								className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold"
								style={{ width: `${width}px` }}
							>
								<span>{formattedValue}</span>
							</div>
						</div>
					);
				})}
			</motion.div>
		</div>
	);
}
