import Slot from "./Slot";

export const Simulator = (): JSX.Element => {
	return (
		<div className="grid grid-cols-2 gap-2 h-full">
			<div className="h-full">
				<Slot title="현금 투입구" />
			</div>
			<div className="grid grid-cols-2 gap-2 h-full">
				<Slot title="신분증 투입구" />
				<Slot title="카드 투입구" />
			</div>
		</div>
	);
};

export default Simulator;
