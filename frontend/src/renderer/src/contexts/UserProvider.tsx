import { useState } from "react";
import { UserContext } from "./UserContext";

export function UserProvider({ children }: { children: React.ReactNode }): JSX.Element {
	const [isElderly, setIsElderly] = useState(false);
	const [isUsingPhone, setIsUsingPhone] = useState(false);
	const [userMsg, setUserMsg] = useState("");
	const [isTalking, setIsTalking] = useState(false);

	return (
		<UserContext.Provider
			value={{
				isElderly,
				setIsElderly,
				isUsingPhone,
				setIsUsingPhone,
				isTalking,
				setIsTalking,
				userMsg,
				setUserMsg
			}}
		>
			{children}
		</UserContext.Provider>
	);
}
