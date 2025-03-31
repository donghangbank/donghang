import AICanvas from "@renderer/components/banker/AICanvas";
import { AIContext } from "@renderer/contexts/AIContext";
import { UserContext } from "@renderer/contexts/UserContext";
import { useContext, useEffect } from "react";
import intro from "@renderer/assets/audios/intro.mp3?url";
import { useNavigate } from "react-router-dom";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";

export const App = (): JSX.Element => {
	const { setAvatarState, setDialogue } = useContext(AIContext);
	const { isUserExist, isElderly } = useContext(UserContext);
	const navigate = useNavigate();

	const { isAudioPlaying } = useActionPlay({
		audioFile: intro,
		dialogue: "안녕하세요, 동행은행입니다.",
		setDialogue,
		setAvatarState,
		shouldActivate: isUserExist,
		avatarState: "bow"
	});

	useEffect(() => {
		if (!isAudioPlaying && isUserExist) {
			if (isElderly === 1) navigate("/general");
			else if (isElderly === 2) navigate("/senior");
		}
	}, [isAudioPlaying, isUserExist, isElderly, navigate]);

	return (
		<div className="w-full h-full flex items-center justify-center">
			<div className="w-full flex items-start justify-center gap-5">
				<AICanvas />
			</div>
		</div>
	);
};

export default App;
