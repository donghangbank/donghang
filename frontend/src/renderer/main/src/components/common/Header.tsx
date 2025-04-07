import { useLocation } from "react-router-dom";
import Logo from "@renderer/assets/logo.png";
import ServiceProgress from "./general/ServiceProgress";
import SERVICE_CONFIG from "@renderer/config/serviceConfig";

export const Header = (): JSX.Element => {
	const { pathname } = useLocation();
	const paths = pathname.split("/").filter(Boolean);
	const currentService = paths[1] ? SERVICE_CONFIG[paths[1]] : null;

	return (
		<div className="h-[10%] p-5 flex items-center">
			{(pathname === "/general" || pathname === "/general/others") && (
				<img className="h-full mr-8" src={Logo} alt="logo" />
			)}

			{currentService && (
				<ServiceProgress
					title={currentService.title}
					steps={currentService.steps}
					pathname={pathname}
				/>
			)}
		</div>
	);
};

export default Header;
