import { cardCheckAPI } from "@renderer/api/transfer";
import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/general/NumberPad";
import { InputContext } from "@renderer/contexts/InputContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatPassword } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { useCallback, useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";

export const DepositCardPasswordPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { setReceivingAccountNumber, password, setPassword } = useContext(InputContext);
	const cardNumber = import.meta.env.VITE_CARD_NUMBER as string;
	const [isPopupVisible, setIsPopupVisible] = useState(false);

	const { mutate: cardCheck } = useMutation({
		mutationFn: () => cardCheckAPI({ cardNumber, password }),
		onSuccess: (data) => {
			setReceivingAccountNumber(data?.fullAccountNumber ?? "");
			navigate("/general/deposit/payment");
		},
		onError: () => {
			setIsPopupVisible(true);
		}
	});

	const handleConfirm = useCallback((): void => {
		if (password.length !== 4) {
			return;
		}
		cardCheck();
	}, [cardCheck, password]);

	useSubMonitorListeners(
		(newVal) => setPassword(newVal),
		handleConfirm,
		() => navigate("/general/final")
	);

	return (
		<div className="flex flex-col gap-6">
			<InputPanel inputValue={password} mainLabel={"비밀번호"} format={formatPassword} />
			<div className="flex gap-6">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad setInputValue={setPassword} type="password" onConfirm={handleConfirm} />
					</div>
				</div>
				{isPopupVisible && (
					<motion.div
						initial={{ opacity: 0, y: 20 }}
						animate={isPopupVisible ? { opacity: 1, y: 0 } : { opacity: 0 }}
						transition={{ duration: 0.5 }}
						className="flex flex-col gap-5 text-3xl bg-white shadow-custom rounded-3xl p-5 h-[45%]"
					>
						<span className="text-red">비밀번호가 올바르지 않습니다.</span>
						<button
							type="button"
							className="p-5 bg-gray-300 rounded-3xl"
							onClick={() => setIsPopupVisible(false)}
						>
							닫기
						</button>
					</motion.div>
				)}
			</div>
		</div>
	);
};

export default DepositCardPasswordPage;
