import { InputContext } from "@renderer/contexts/InputContext";
import { ProductContext } from "@renderer/contexts/ProductContext";
import { SpecSheetContext } from "@renderer/contexts/SpecSheetContext";
import { useActionPlay } from "@renderer/hooks/ai/useActionPlay";
import { useContextReset } from "@renderer/hooks/useContextReset";
import { useQueryClient } from "@tanstack/react-query";
import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const FinalPage = (): JSX.Element => {
	const navigate = useNavigate();
	const { resetAll } = useContext(InputContext);
	const { resetAll: productResetAll } = useContext(ProductContext);
	const { resetSpecSheet } = useContext(SpecSheetContext);
	const { resetContext } = useContextReset();
	const queryClient = useQueryClient();

	useEffect(() => {
		// 1. 모든 Context 상태 초기화
		resetAll();
		productResetAll();
		resetSpecSheet();
		resetContext();

		// 2. 서브 윈도우 초기화
		window.mainAPI.updateSubState(false); // 서브 윈도우 로고 표시
		window.mainAPI.updateSubType("password"); // 기본 타입 복구
		window.mainAPI.notifyMainNumberChange(""); // 입력 값 초기화
		window.mainAPI.updateSubDisabled(false); // 버튼 활성화

		// 3. 쿼리 클라이언트 초기화
		queryClient.clear(); // 모든 쿼리 무효화

		// 4. 2초 후 홈으로 이동
		const timer = setTimeout(() => navigate("/"), 2000);

		return (): void => clearTimeout(timer);
	}, [navigate, resetAll, resetSpecSheet, resetContext, queryClient, productResetAll]);

	useActionPlay({
		audioFile: "thank_you.mp3",
		dialogue: "감사합니다.",
		shouldActivate: true,
		avatarState: "bow"
	});

	return (
		<div className="flex flex-col h-full  p-10">
			<div className="flex-1 flex flex-col items-center justify-center w-full bg-white p-10 rounded-2xl shadow-custom">
				<span className="text-6xl font-bold text-center leading-snug">
					감사합니다
					<br />
					놓고 가신 물건이 없는지
					<br />
					확인해주시기 바랍니다
				</span>
			</div>
		</div>
	);
};

export default FinalPage;
