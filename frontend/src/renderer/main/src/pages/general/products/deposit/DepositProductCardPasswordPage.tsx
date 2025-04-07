import { cardCheckAPI } from "@renderer/api/transfer";
import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/general/NumberPad";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { formatPassword } from "@renderer/utils/formatters";
import { useMutation } from "@tanstack/react-query";
import { useCallback, useContext, useState } from "react";
import { useNavigate } from "react-router-dom";

export const DepositProductCardPasswordPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { setWithdrawalAccountNumber, setPayoutAccountNumber, password, setPassword, setMemberId } =
		useContext(ProductContext);
	const cardNumber = "1234567812345678";
	const [isPopupVisible, setIsPopupVisible] = useState(false);

	const { mutate: cardCheck } = useMutation({
		mutationFn: () => cardCheckAPI({ cardNumber, password }),
		onSuccess: (data) => {
			setWithdrawalAccountNumber(data.fullAccountNumber);
			setPayoutAccountNumber(data.fullAccountNumber);
			setMemberId(data.ownerId);
			navigate("/general/depositproducts/info/amount");
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
					<div className="flex flex-col gap-5 text-3xl bg-white shadow-custom rounded-3xl p-5 h-[45%]">
						<span className="text-red">비밀번호가 올바르지 않습니다.</span>
						<button
							type="button"
							className="p-5 bg-gray-300 rounded-3xl"
							onClick={() => setIsPopupVisible(false)}
						>
							닫기
						</button>
					</div>
				)}
			</div>
		</div>
	);
};

export default DepositProductCardPasswordPage;
