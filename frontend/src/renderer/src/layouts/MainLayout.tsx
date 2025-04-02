import AICanvas from "@renderer/components/banker/AICanvas";
import Header from "@renderer/components/common/Header";
import MainNumberPad from "@renderer/components/common/MainNumberPad";
import Simulator from "@renderer/components/common/simulator/Simulator";
import inputLinkMapping from "@renderer/config/inputLinkMapping";
import { InputContext } from "@renderer/contexts/InputContext";
import { useContext } from "react";
import { Outlet, useLocation } from "react-router-dom";
import logo from "@renderer/assets/logo.png";

export const MainLayout = (): JSX.Element => {
	const location = useLocation();
	const isSenior = location.pathname === "/" || location.pathname.includes("/senior");
	const { setPassword, setAccount, setAmount, setResidentNumber, setConfirmTrigger } =
		useContext(InputContext);

	const inputLink = inputLinkMapping[location.pathname] || "";

	let padType = "";
	let setInputValue: React.Dispatch<React.SetStateAction<string>> = () => {};

	if (location.pathname.includes("general")) {
		if (location.pathname.includes("password")) {
			padType = "password";
			setInputValue = setPassword;
		} else if (location.pathname.includes("amount")) {
			padType = "amount";
			setInputValue = setAmount;
		} else if (location.pathname.includes("resident")) {
			padType = "resident";
			setInputValue = setResidentNumber;
		} else if (location.pathname.includes("account")) {
			padType = "account";
			setInputValue = setAccount;
		}
	}

	const handleMainConfirm = (): void => {
		setConfirmTrigger(-1);
	};

	return (
		<div className="w-screen h-screen flex flex-col">
			{/* 상단 화면 */}
			<div className="w-full h-1/2 relative">
				{isSenior ? (
					<div className="w-full h-[90%] shadow-md">
						<div className="absolute w-full h-full">
							<AICanvas />
						</div>
						<Outlet />
					</div>
				) : (
					<div className="w-full h-[90%] flex flex-col">
						<Header />
						<div className="flex-1 flex">
							<div className="w-[50vw]">
								<AICanvas />
							</div>
							<div className="w-[50vw] flex flex-col items-center justify-center p-10">
								<Outlet />
							</div>
						</div>
					</div>
				)}
				<div className="absolute bottom-0 w-full h-[10%] bg-gray-400 p-2">
					<Simulator />
				</div>
			</div>

			{/* 하단 화면 */}
			<div className="w-full h-1/2 flex items-center justify-center bg-gray-200">
				{!inputLink ? (
					<img src={logo} alt="logo" />
				) : (
					<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[480px]">
						<div className="flex flex-col gap-2.5 w-[460px] h-[740px] text-5xl font-bold">
							<MainNumberPad
								setInputValue={setInputValue}
								type={padType}
								onConfirm={handleMainConfirm}
							/>
						</div>
					</div>
				)}
			</div>
		</div>
	);
};

export default MainLayout;
