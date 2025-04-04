// Dialogue.tsx
import { motion, AnimatePresence } from "framer-motion";

interface DialogueProps {
	text: string;
}

export default function Dialogue({ text }: DialogueProps): JSX.Element {
	return (
		<AnimatePresence>
			{text && (
				<motion.div
					initial={{ opacity: 0, y: 20, scale: 0.9 }}
					transition={{ duration: 0.5 }}
					animate={{ opacity: 1, y: 0, scale: 1 }}
					exit={{ opacity: 0, y: -20, scale: 0.9 }}
					className="bg-white rounded-full shadow-[0_2px_12px_rgba(178,191,227,1)] px-8 py-2 flex justify-center items-center"
				>
					<div className="text-3xl font-bold">{text}</div>
				</motion.div>
			)}
		</AnimatePresence>
	);
}
