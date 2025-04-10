import { UserContext } from "@renderer/contexts/UserContext";
import { useContext } from "react";

export default function StatusLight(): JSX.Element {
	const { isElderly, isUsingPhone, isTalking } = useContext(UserContext);

	const Light = ({
		active,
		activeColor = "bg-blue",
		inactiveColor = "bg-red"
	}: {
		active: boolean;
		activeColor?: string;
		inactiveColor?: string;
	}): JSX.Element => (
		<div className="flex items-center justify-center">
			<div
				className={`
					w-4 h-4 rounded-full 
					${active ? activeColor : inactiveColor} 
					transition-all duration-300
				`}
			/>
		</div>
	);

	return (
		<div className="h-full w-12 flex flex-col gap-2 p-2 items-center">
			<Light active={isElderly === 2} />
			<Light active={isUsingPhone} />
			<Light active={isTalking} activeColor="bg-yellow-400" inactiveColor="bg-gray-300" />
		</div>
	);
}
