import { motion } from "framer-motion";

interface SubConfirmProps {
	label?: string;
	onClick?: () => void;
}

export default function SubConfirm({ label = "확인", onClick }: SubConfirmProps): JSX.Element {
	const handleConfirmClick = (): void => {
		if (onClick) {
			onClick();
		} else {
			window.subAPI.notifyButtonAction("confirm");
		}
	};

	return (
		<motion.div
			initial={{ opacity: 0, y: 100 }}
			animate={{ opacity: 1, y: 0 }}
			exit={{ opacity: 0, y: 100 }}
			className="flex flex-col justify-between"
		>
			<div className="h-[20%] flex justify-center w-full text-5xl text-white">
				<button
					onClick={handleConfirmClick}
					className="block bg-blue p-8 min-h-52 min-w-[400px] rounded-3xl shadow-custom items-center justify-center"
				>
					<div className="w-full h-full inline-flex items-center justify-center gap-8">
						<span>{label}</span>
					</div>
				</button>
			</div>
		</motion.div>
	);
}
