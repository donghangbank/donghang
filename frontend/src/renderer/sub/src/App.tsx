import logo from "@renderer/assets/logo.png";
import { SubNumberPad } from "@renderer/components/common/SubNumberPad";
import { useEffect, useState } from "react";

declare global {
	interface Window {
		subAPI: {
			notifyNumberChange: (value: string) => void;
			onMainNumberUpdate: (callback: (value: string) => void) => void;
			notifyButtonAction: (action: "confirm" | "cancel") => void;
			onInputLinkUpdated: (callback: (hasInput: boolean) => void) => void;
			onSubTypeUpdate: (callback: (newType: string) => void) => void;
			onDisabledUpdate: (value: boolean) => void;
		};
	}
}
export default function App(): JSX.Element {
	const [localValue, setLocalValue] = useState("");
	const [showButton, setShowButton] = useState(false);
	const [subType, setSubType] = useState<"password" | "account" | "resident" | "amount">(
		"password"
	);

	useEffect(() => {
		// 메인 프로세스 → "sub-inputlink-updated"(hasInputLink) 이벤트 받음
		window.subAPI.onInputLinkUpdated((hasInput) => {
			setShowButton(hasInput);
		});

		window.subAPI.onSubTypeUpdate((newType) => {
			// eslint-disable-next-line @typescript-eslint/no-explicit-any
			setSubType(newType as any);
			setLocalValue("");
		});
	}, []);

	return (
		<div className="w-screen h-screen flex flex-col">
			<div className="w-full h-full flex items-center justify-center bg-gray-200">
				{showButton ? (
					<div className="p-2.5 bg-white shadow-custom rounded-3xl w-[480px]">
						<div className="flex flex-col gap-2.5 w-[460px] h-[740px] text-5xl font-bold">
							<SubNumberPad type={subType} localValue={localValue} setLocalValue={setLocalValue} />
						</div>
					</div>
				) : (
					<img src={logo} alt="logo" />
				)}
			</div>
		</div>
	);
}
