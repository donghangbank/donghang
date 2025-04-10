import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContext, useEffect } from "react";
import { AIContext } from "@renderer/contexts/AIContext";
import { useNavigate } from "react-router-dom";
import card_or_account_or_account_num from "@renderer/assets/audios/card_or_account_or_account_num.mp3?url";

export default function SeniorDepositProductOption(): JSX.Element {
	const { construction } = useContext(AIContext);
	const navigate = useNavigate();

	useActionPlay({
		audioFile: card_or_account_or_account_num,
		dialogue: "본인 확인이 필요합니다! 카드, 통장, 계좌번호 중 가지고 오신게 있으실까요?",
		shouldActivate: true,
		avatarState: "idle"
	});

	useEffect(() => {
		if (construction === "카드선택") {
			navigate("/senior/depositproducts/warning/card");
		}
	}, [construction, navigate]);

	return <></>;
}
