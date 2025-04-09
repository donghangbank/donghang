interface AudioManagePanelProps {
	onConfirm: () => void;
	onRestart: () => void;
}

export const AudioManagePanel = ({ onConfirm, onRestart }: AudioManagePanelProps): JSX.Element => {
	return (
		<div className="flex flex-col w-full h-full justify-center items-center">
			<div className="h-[20%] grid grid-cols-2 gap-2.5 text-6xl font-bold shadow-custom text-white bg-white rounded-3xl p-6">
				<button onClick={onConfirm} className="block">
					<div className="w-full h-full bg-green inline-flex items-center justify-center rounded-3xl px-6">
						<span>확인</span>
					</div>
				</button>
				<button onClick={onRestart} className="block">
					<div className="w-full h-full bg-red inline-flex items-center justify-center rounded-3xl px-6">
						<span>다시 말하기</span>
					</div>
				</button>
			</div>
		</div>
	);
};

export default AudioManagePanel;
