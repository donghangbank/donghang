import { AIContext } from "@renderer/contexts/AIContext";
import { PageContext } from "@renderer/contexts/PageContext";
import { UserContext } from "@renderer/contexts/UserContext";
import { useContext } from "react";

interface UseContextResetReturn {
	resetContext: () => void;
}

export const useContextReset = (): UseContextResetReturn => {
	const { setIsElderly, setIsTalking, setIsUserExist, setIsUsingPhone, setUserMsg } =
		useContext(UserContext);
	const { setAvatarState, setConstruction, setDialogue } = useContext(AIContext);
	const { setCurrentJob } = useContext(PageContext);

	const resetContext = (): void => {
		setIsUserExist(false);
		setIsElderly(0);
		setIsUsingPhone(false);
		setIsTalking(false);
		setUserMsg("");
		setAvatarState("idle");
		setConstruction("etc");
		setDialogue("");
		setCurrentJob("");
	};

	return { resetContext };
};
