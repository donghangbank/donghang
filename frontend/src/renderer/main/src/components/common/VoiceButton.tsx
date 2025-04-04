import AudioVisualizer from "./AudioVisualizer";

export const VoiceButton = (): JSX.Element => {
	return (
		<div className="inline-flex items-center px-5 py-10 rounded-full bg-purple-linear animate-pulse self-center">
			<AudioVisualizer />
			<span className="ml-3 text-white text-5xl font-bold">듣고 있어요</span>
		</div>
	);
};
