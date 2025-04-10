export const AudioManagePanel = (): JSX.Element => {
	const handleConfirmClick = (): void => {
		// 서브 로직이 있다면 수행
		// 그 후 메인 쪽에 "confirm" 알림
		window.subAPI.notifyButtonAction("confirm");
	};

	const handleCancelClick = (): void => {
		// 서브 로직(필요하다면 수행)
		// 그 후 메인 쪽에 "cancel" 알림
		window.subAPI.notifyButtonAction("cancel");
	};
	return (
		<div className="flex flex-col w-full h-full justify-center items-center">
			<div className="h-[20%] grid grid-cols-2 gap-2.5 text-6xl font-bold shadow-custom text-white bg-white rounded-3xl p-6">
				<button onClick={handleConfirmClick} className="block">
					<div className="w-full h-full bg-green inline-flex items-center justify-center rounded-3xl px-6">
						<span>확인</span>
					</div>
				</button>
				<button onClick={handleCancelClick} className="block">
					<div className="w-full h-full bg-red inline-flex items-center justify-center rounded-3xl px-6">
						<span>다시 말하기</span>
					</div>
				</button>
			</div>
		</div>
	);
};

export default AudioManagePanel;
