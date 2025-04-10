import { accountCheckAPI } from "@renderer/api/transfer";
import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/general/NumberPad";
import { InputContext } from "@renderer/contexts/InputContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatAccountNumber, formatPassword } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { motion } from "framer-motion";
import { useCallback, useContext, useState } from "react";
import { useNavigate } from "react-router-dom";

interface CardPasswordProps {
	password: string;
	setPassword: React.Dispatch<React.SetStateAction<string>>;
	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	onSuccess: (data: any) => void;
	successNavigatePath: string;
	errorMessage?: string;
}

export const AccountPassword = ({
	password,
	setPassword,
	onSuccess,
	successNavigatePath
}: CardPasswordProps): JSX.Element => {
	const navigate = useNavigate();
	const [isPopupVisible, setIsPopupVisible] = useState(false);
	const { sendingAccountNumber } = useContext(InputContext);

	const { mutate: cardCheck } = useMutation({
		mutationFn: () => accountCheckAPI({ accountNumber: sendingAccountNumber, password }),
		onSuccess: (data) => {
			onSuccess(data);
			navigate(successNavigatePath);
		},
		onError: () => {
			setIsPopupVisible(true);
		}
	});

	const handleConfirm = useCallback((): void => {
		if (password.length !== 4) return;
		cardCheck();
	}, [cardCheck, password]);

	useSubMonitorListeners(
		(newVal) => setPassword(newVal),
		handleConfirm,
		() => navigate("/general/final")
	);

	return (
		<div className="flex flex-col gap-6">
			<InputPanel
				inputValue={sendingAccountNumber}
				mainLabel={"받으시는 분 계좌번호"}
				format={formatAccountNumber}
				isCount={false}
			/>
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
						className="flex flex-col gap-5 text-3xl bg-white shadow-custom rounded-3xl p-5 h-[52%]"
					>
						<span className="text-red text-center">
							계좌번호 또는 비밀번호가
							<br />
							올바르지 않습니다.
						</span>
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

export default AccountPassword;
