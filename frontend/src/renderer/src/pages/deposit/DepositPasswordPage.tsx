import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/NumberPad";
import { formatPassword } from "@renderer/utils/formatters";
import { useState } from "react";

export const DepositPasswordPage = (): JSX.Element => {
	const [inputValue, setInputValue] = useState("");

	return (
		<div className="flex h-full">
			<div className="h-full" style={{ width: "66.67vw" }}>
				<InputPanel
					inputValue={inputValue}
					mainLabel={"비밀번호"}
					subLabel={"비밀번호"}
					format={formatPassword}
				/>
			</div>
			<div className="h-full" style={{ width: "33.33vw" }}>
				<NumberPad setInputValue={setInputValue} type="password" link="/deposit/auth" />
			</div>
		</div>
	);
};

export default DepositPasswordPage;
