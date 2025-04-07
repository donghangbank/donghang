import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContext, useEffect } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { useNavigate } from "react-router-dom";
import TestButton from "@renderer/components/common/senior/TestButton";
import { PageContext } from "@renderer/contexts/PageContext";

interface JobCheckProps {
	job: string;
	audioFile?: string;
	prev: string;
	link: string;
}

export default function JobChoiceCheck({ job, audioFile, prev, link }: JobCheckProps): JSX.Element {
	const { construction } = useContext(AIContext);
	const { setCurrentJob } = useContext(PageContext);
	const navigate = useNavigate();

	useActionPlay({
		audioFile: audioFile,
		dialogue: `${job} 도와드리면 될까요?`,
		shouldActivate: true,
		avatarState: "idle"
	});

	useEffect(() => {
		if (construction === "긍정") {
			navigate(link);
			setCurrentJob(job);
		} else if (construction === "부정" || construction === "홈") {
			navigate("/senior/final");
		}
	}, [construction, navigate, setCurrentJob, job, link]);

	useEffect(() => {
		setCurrentJob(job);
	}, [setCurrentJob, job]);

	return (
		<>
			<TestButton prevRoute={prev} nextRoute={link} />
		</>
	);
}
