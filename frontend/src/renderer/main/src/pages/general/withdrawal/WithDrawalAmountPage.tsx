import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/general/NumberPad";
import { formatAccountNumber } from "@renderer/utils/formatters";
import { useState } from "react";

export const WithDrawalAmountPage = (): JSX.Element => {
	const [inputValue, setInputValue] = useState("");

	return (
		<div className="flex h-full">
			<div className="h-full" style={{ width: "66.67vw" }}>
				<InputPanel
					inputValue={inputValue}
					mainLabel={"금액"}
					subLabel={"꺼내는 금액"}
					format={formatAccountNumber}
				/>
			</div>
			<div className="h-full" style={{ width: "33.33vw" }}>
				<NumberPad setInputValue={setInputValue} type="amount" link="/general/withdrawal/confirm" />
			</div>
		</div>
	);
};

export default WithDrawalAmountPage;
