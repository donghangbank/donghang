import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import TestButton from "@renderer/components/common/senior/TestButton";
import { Link } from "react-router-dom";
import { formatAmount } from "@renderer/utils/formatters";
import { useContext } from "react";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import billCheck from "@renderer/assets/audios/bill_check.mp3?url";
import { motion } from "framer-motion";

export default function SeniorTransferInfoSpecSheetPage(): JSX.Element {
	const { amount, recipientName, sendingAccountBalance } = useContext(SpecSheetContext);

	useActionPlay({
		audioFile: billCheck,
		dialogue: "명세표를 확인해주세요",
		shouldActivate: true,
		avatarState: "idle"
	});

	const isReady = !!amount && !!recipientName && sendingAccountBalance !== null;

	return (
		<div className="flex w-full h-full justify-end items-center">
			<TestButton prevRoute="/senior/transfer/warning/scam" nextRoute="/senior/transfer/option" />

			<motion.div
				initial={{ opacity: 0, y: 20 }}
				animate={isReady ? { opacity: 1, y: 0 } : { opacity: 0 }}
				transition={{ duration: 0.5 }}
				className="bg-white p-5 flex flex-col gap-6 mr-24 rounded-3xl"
			>
				<div className="flex flex-col gap-6 justify-center items-start">
					<span className="text-blue text-3xl font-bold">받는 사람</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[400px]">
						<span>{recipientName}</span>
					</div>
				</div>

				<div className="flex flex-col gap-6 justify-center items-start">
					<span className="text-blue text-3xl font-bold">잔액</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[400px]">
						<span>{formatAmount(String(sendingAccountBalance))}</span>
					</div>
				</div>

				<div className="flex flex-col gap-6 justify-center items-start">
					<span className="text-blue text-3xl font-bold">{recipientName} 님께 보낸 돈</span>
					<div className="bg-cloudyBlue text-3xl p-5 text-right rounded-3xl font-bold w-[400px]">
						<span>{formatAmount(String(amount))}</span>
					</div>
				</div>

				<div className="flex justify-center items-center">
					<button type="button" className="p-6 bg-blue rounded-xl w-full">
						<Link to={"/senior/final"}>
							<span className="text-3xl text-white">확인</span>
						</Link>
					</button>
				</div>
			</motion.div>
		</div>
	);
}
