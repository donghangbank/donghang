import Simulator from "@renderer/components/common/simulator/Simulator";
import { Outlet } from "react-router-dom";

export const MainLayout = (): JSX.Element => {
	return (
		<div className="w-screen h-screen flex flex-col">
			<div className="w-full h-[90%] shadow-md">
				<Outlet />
			</div>

			<div className="w-full h-[10%] bg-gray-400 p-2">
				<Simulator />
			</div>
		</div>
	);
};

export default MainLayout;
