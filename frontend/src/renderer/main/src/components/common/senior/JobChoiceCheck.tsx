import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContext, useEffect, useState } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { useNavigate } from "react-router-dom";
import { PageContext } from "@renderer/contexts/PageContext";

interface JobCheckProps {
	job: string;
	audioFile?: string;
	link: string;
}

export default function JobChoiceCheck({ job, audioFile, link }: JobCheckProps): JSX.Element {
	const { construction, setConstruction } = useContext(AIContext);
	const { setCurrentJob } = useContext(PageContext);
	const navigate = useNavigate();
	const [voiceEnd, setVoiceEnd] = useState(false);

	useActionPlay({
		audioFile: audioFile,
		dialogue: `${job} 도와드리면 될까요?`,
		shouldActivate: true,
		avatarState: "idle",
		onComplete: () => {
			setVoiceEnd(true);
		}
	});

	useEffect(() => {
		if (!voiceEnd) return;
		if (construction === "긍정") {
			setCurrentJob(job);
			navigate(link);
		} else if (construction === "부정" || construction === "홈") {
			setCurrentJob("");
			setConstruction("etc");
			navigate("/senior");
		}
	}, [construction, navigate, setCurrentJob, job, link, voiceEnd, setConstruction]);

	useEffect(() => {
		setCurrentJob(job);
	}, [setCurrentJob, job]);

	return <></>;
}
