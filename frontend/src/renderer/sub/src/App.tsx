// sub/index.tsx
import logo from "@renderer/assets/logo.png";
import SubCardWarning from "@renderer/components/common/SubCardWarning";
import { SubNumberPad } from "@renderer/components/common/SubNumberPad";
import SubScamWarning from "@renderer/components/common/SubScamWarning";
import { useEffect, useState } from "react";

declare global {
	interface Window {
		subAPI: {
			notifyNumberChange: (value: string) => void;
			onMainNumberUpdate: (callback: (value: string) => void) => void;
			notifyButtonAction: (action: "confirm" | "cancel") => void;
			onInputLinkUpdated: (callback: (hasInput: boolean) => void) => void;
			onSubTypeUpdate: (callback: (newType: string) => void) => void;
			onDisabledUpdate: (callback: (value: boolean) => void) => void;
			// eslint-disable-next-line @typescript-eslint/no-explicit-any
			onSubModeUpdate: (callback: (data: { mode: string; data?: any }) => void) => void;
		};
	}
}

export default function App(): JSX.Element {
	const [localValue, setLocalValue] = useState("");
	const [showButton, setShowButton] = useState(false);
	const [subType, setSubType] = useState<"password" | "account" | "amount" | "day">("password");
	const [mode, setMode] = useState<"numpad" | "scam-warning" | "card-warning" | "default">(
		"default"
	);

	useEffect(() => {
		window.subAPI.onInputLinkUpdated((hasInput) => {
			setShowButton(hasInput);
		});

		window.subAPI.onSubTypeUpdate((newType) => {
			setSubType(newType as "password" | "account" | "amount" | "day");
			setLocalValue("");
		});

		window.subAPI.onSubModeUpdate(({ mode }) => {
			setMode(mode as "numpad" | "scam-warning" | "card-warning" | "default");
			if (mode === "numpad") {
				setShowButton(true);
			} else {
				setShowButton(false);
			}
		});
	}, []);

	return (
		<div className="w-screen h-screen flex flex-col">
			<div className="w-full h-full flex items-center justify-center bg-gray-200">
				{mode === "numpad" && showButton ? (
					<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[480px]">
						<div className="flex flex-col gap-2.5 w-[460px] h-[740px] text-5xl font-bold">
							<SubNumberPad type={subType} localValue={localValue} setLocalValue={setLocalValue} />
						</div>
					</div>
				) : mode === "scam-warning" ? (
					<SubScamWarning />
				) : mode === "card-warning" ? (
					<SubCardWarning />
				) : (
					<img src={logo} alt="logo" />
				)}
			</div>
		</div>
	);
}
