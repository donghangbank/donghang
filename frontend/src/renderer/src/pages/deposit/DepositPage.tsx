import InputPanel from "@renderer/components/common/InputPannel";
import NumberPad from "@renderer/components/common/NumberPad";

export const DepositPage: React.FC = () => {
	return (
		<div className="flex h-screen">
			<div style={{ width: "66.67vw" }}>
				<InputPanel
				/>
			<div style={{ width: "33.33vw" }}>
				<NumberPad />
			</div>
		</div>
	);
};

export default DepositPage;
