import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/NumberPad";
import { formatAccountNumber } from "@renderer/utils/formatters";
import { useState } from "react";

export const DepositPage = (): JSX.Element => {
	const [inputValue, setInputValue] = useState("");

	return (
		<div className="flex h-full">
			<div className="h-full" style={{ width: "66.67vw" }}>
				<InputPanel
					inputValue={inputValue}
					mainLabel={"계좌번호"}
					subLabel={"본인 계좌"}
					format={formatAccountNumber}
				/>
			</div>
			<div className="h-full" style={{ width: "33.33vw" }}>
				<NumberPad setInputValue={setInputValue} type="account" />
			</div>
		</div>
	);
};

export default DepositPage;
