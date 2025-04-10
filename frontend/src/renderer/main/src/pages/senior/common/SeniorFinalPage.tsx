import { InputContext } from "@renderer/contexts/InputContext";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContextReset } from "@renderer/hooks/useContextReset";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import thank from "@renderer/assets/audios/thank.mp3?url";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { UserContext } from "@renderer/contexts/UserContext";
// import { useSubMonitorListeners } from "@renderer/hooks/useSubMonitorListeners";
import { useQueryClient } from "@tanstack/react-query";
import { AIContext } from "@renderer/contexts/AIContext";

export function SeniorFinalPage(): JSX.Element {
	const navigate = useNavigate();
	const { resetAll } = useContext(InputContext);
	const { resetSpecSheet } = useContext(SpecSheetContext);
	const { resetContext } = useContextReset();
	const { resetAll: productResetAll } = useContext(ProductContext);
	const { resetAll: userResetAll } = useContext(UserContext);
	const queryClient = useQueryClient();
	const { resetAIContext } = useContext(AIContext);

	// useSubMonitorListeners(
	// 	() => {},
	// 	() => {},
	// 	() => {}
	// );

	useActionPlay({
		audioFile: thank,
		dialogue: "감사합니다.",
		shouldActivate: true,
		avatarState: "bow",
		onComplete: () => {
			// 1. 모든 Context 상태 초기화
			resetAll();
			productResetAll();
			resetSpecSheet();
			resetContext();
			userResetAll();
			resetAIContext();
			queryClient.clear(); // 모든 쿼리 무효화

			// 2. 서브 윈도우 초기화
			window.mainAPI.updateSubState(false); // 서브 윈도우 로고 표시
			window.mainAPI.updateSubType("password"); // 기본 타입 복구
			window.mainAPI.notifyMainNumberChange(""); // 입력 값 초기화
			window.mainAPI.updateSubDisabled(false); // 버튼 활성화
			window.mainAPI.onCallCancel(() => {});
			window.mainAPI.onCallConfirm(() => {});

			const forceReload = (): void => {
				window.location.href = "index.html";
				window.location.reload();
			};

			// 3. 2초 후 홈으로 이동
			setTimeout(() => {
				navigate("/");
				forceReload();
			}, 3000);
		}
	});

	return <></>;
}
