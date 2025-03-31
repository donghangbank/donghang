import { useState } from "react";
import { UserContext } from "./UserContext";

export function UserProvider({ children }: { children: React.ReactNode }): JSX.Element {
	const [isUserExist, setIsUserExist] = useState(false);
	const [isElderly, setIsElderly] = useState(0);
	const [isUsingPhone, setIsUsingPhone] = useState(false);
	const [userMsg, setUserMsg] = useState("");
	const [isTalking, setIsTalking] = useState(false);

	return (
		<UserContext.Provider
			value={{
				isUserExist,
				setIsUserExist,
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
