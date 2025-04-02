import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/general/NumberPad";
import { InputContext } from "@renderer/contexts/InputContext";
import { formatPassword } from "@renderer/utils/formatters";
import { useCallback, useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const TransferCardPasswordPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { password, setPassword, confirmTrigger, setConfirmTrigger } = useContext(InputContext);

	const handleConfirm = useCallback((): void => {
		if (password.length !== 4) {
			return;
		}
		navigate("/general/transfer/info/account");
	}, [navigate, password]);

	useEffect(() => {
		if (confirmTrigger === -1) {
			handleConfirm();

			setConfirmTrigger(0);
		}
	}, [confirmTrigger, setConfirmTrigger, handleConfirm]);

	return (
		<div className="flex flex-col gap-6">
			<InputPanel inputValue={password} mainLabel={"비밀번호"} format={formatPassword} />
			<div className="flex">
				<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[240px]">
					<div className="flex flex-col gap-2.5 w-[220px] h-[370px] text-3xl font-bold">
						<NumberPad setInputValue={setPassword} type="password" onConfirm={handleConfirm} />
					</div>
				</div>
			</div>
		</div>
	);
};

export default TransferCardPasswordPage;
