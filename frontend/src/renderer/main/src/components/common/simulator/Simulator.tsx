import CashSlot from "./CashSlot";
import CardSlot from "./CardSlot";
import ResidentSlot from "./ResidentSlot";
import StatusLight from "./StatusLight";

export const Simulator = (): JSX.Element => {
	return (
		<div className="grid grid-cols-2 gap-2 h-full ">
			<div className="h-full flex flex-row">
				<StatusLight />
				<CashSlot />
			</div>
			<div className="h-full grid gap-2 grid-cols-2">
				<ResidentSlot />
				<CardSlot />
			</div>
		</div>
	);
};

export default Simulator;
