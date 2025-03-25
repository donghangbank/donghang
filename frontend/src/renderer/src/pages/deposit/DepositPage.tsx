import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/NumberPad";
import { formatAccountNumber } from "@renderer/utils/formatters";

export const DepositPage: React.FC = () => {
	return (
		<div className="flex h-screen">
			<div style={{ width: "66.67vw" }}>
				<InputPanel
					format={formatAccountNumber}
				/>
			<div style={{ width: "33.33vw" }}>
				<NumberPad />
			</div>
		</div>
	);
};

export default DepositPage;
