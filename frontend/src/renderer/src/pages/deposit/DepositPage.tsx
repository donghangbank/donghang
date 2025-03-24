import NumberPad from "@renderer/components/common/NumberPad";

export const DepositPage: React.FC = () => {
	return (
		<div className="flex h-screen">
			<div style={{ width: "66.67vw" }}>왼쪽 영역</div>
			<div style={{ width: "33.33vw" }}>
				<NumberPad />
			</div>
		</div>
	);
};

export default DepositPage;
