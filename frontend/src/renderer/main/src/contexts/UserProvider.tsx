import { useState } from "react";
import { UserContext } from "./UserContext";

export function UserProvider({ children }: { children: React.ReactNode }): JSX.Element {
	const [isUserExist, setIsUserExist] = useState(false);
	const [isElderly, setIsElderly] = useState(0);
	const [isUsingPhone, setIsUsingPhone] = useState(false);
	const [userMsg, setUserMsg] = useState("");
	const [isTalking, setIsTalking] = useState(false);
	const [isNotFishing, setIsNotFishing] = useState(false);

	const resetAll = (): void => {
		setIsUserExist(false);
		setIsElderly(0);
		setIsUsingPhone(false);
		setUserMsg("");
		setIsTalking(false);
		setIsNotFishing(false);
	};

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
				setUserMsg,
				isNotFishing,
				setIsNotFishing,
				resetAll
			}}
		>
			{children}
		</UserContext.Provider>
	);
}
