const AudioVisualizer = (): JSX.Element => {
	return (
		<svg width="60" height="60" viewBox="0 0 60 60" xmlns="http://www.w3.org/2000/svg">
			<title>AudioVisualizer</title>
			<rect x="5" y="10" width="10" height="40" fill="#fff">
				<animate attributeName="height" values="40;20;40" dur="1s" repeatCount="indefinite" />
				<animate attributeName="y" values="10;20;10" dur="1s" repeatCount="indefinite" />
			</rect>
			<rect x="25" y="5" width="10" height="50" fill="#fff">
				<animate
					attributeName="height"
					values="50;10;50"
					dur="1s"
					begin="0.2s"
					repeatCount="indefinite"
				/>
				<animate attributeName="y" values="5;25;5" dur="1s" begin="0.2s" repeatCount="indefinite" />
			</rect>
			<rect x="45" y="15" width="10" height="30" fill="#fff">
				<animate
					attributeName="height"
					values="30;50;10;30"
					dur="1s"
					begin="0.4s"
					repeatCount="indefinite"
				/>
				<animate
					attributeName="y"
					values="15;5;25;15"
					dur="1s"
					begin="0.4s"
					repeatCount="indefinite"
				/>
			</rect>
		</svg>
	);
};

export default AudioVisualizer;
